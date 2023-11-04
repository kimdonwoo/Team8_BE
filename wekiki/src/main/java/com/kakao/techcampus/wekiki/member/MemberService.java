package com.kakao.techcampus.wekiki.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kakao.techcampus.wekiki._core.error.exception.*;
import com.kakao.techcampus.wekiki._core.jwt.JWTTokenProvider;
import com.kakao.techcampus.wekiki._core.utils.RedisUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.kakao.techcampus.wekiki._core.utils.SecurityUtils.currentMember;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberJPARepository memberRepository;
    private final JWTTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtility redisUtility;
    private final JavaMailSender javaMailSender;
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.redirect.uri}")
    private String KAKAO_REDIRECT_URI;


    public void signUp(MemberRequest.signUpRequestDTO signUpRequestDTO) {

        Optional<Member> checkMember = memberRepository.findByEmail(signUpRequestDTO.getEmail());
        if (checkMember.isPresent()){
            log.error("이미 존재하는 회원이 가입 요청하였습니다.");
            throw new Exception400("이미 존재하는 회원입니다.");
        }

        Member member = Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .name(signUpRequestDTO.getNickName())
                .created_at(LocalDateTime.now())
                .authority(Authority.user)
                .build();
        memberRepository.save(member);
    }

    public MemberResponse.authTokenDTO login(MemberRequest.loginRequestDTO loginDTO) {
        Optional<Member> loginMember = memberRepository.findByEmail(loginDTO.getEmail());
        if(loginMember.isEmpty()) {
            log.error("없는 회원이 로그인 요청을 하였습니다.");
            throw new Exception404("존재하지 않는 이메일입니다.");
        }
        if(!passwordEncoder.matches(loginDTO.getPassword(), loginMember.get().getPassword())){
            log.error("로그인 요청 회원의 비밀번호가 틀렸습니다. User Id : " + loginMember.get().getEmail());
            throw new Exception400("비밀번호가 틀렸습니다.");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        AuthenticationManager manager = authenticationManagerBuilder.getObject();
        Authentication authentication = manager.authenticate(usernamePasswordAuthenticationToken);
        return tokenProvider.generateToken(authentication);
    }

    public MemberResponse.myInfoResponseDTO getMyInfo() {
        Member member;
        try {
            member = findMember();
        } catch (Exception404 e) {
            log.error("Access Token에서 뽑아낸 회원이 존재하지 않는 회원입니다. (마이페이지 조회)");
            throw e;
        }
        List<MemberResponse.myInfoResponseDTO.myInfoGroupDTO> infoGroupDTOList = member.getGroupMembers().stream()
                .map(groupMember -> {
                    return new MemberResponse.myInfoResponseDTO.myInfoGroupDTO(groupMember, groupMember.getGroup());
                }).collect(Collectors.toList());
        return new MemberResponse.myInfoResponseDTO(member.getName(), infoGroupDTOList);
    }

    public void cancel() {
        Member member;
        try {
            member = findMember();
        } catch (Exception404 e) {
            log.error("Access Token에서 뽑아낸 회원이 존재하지 않는 회원입니다. (회원 탈퇴)");
            throw e;
        }
        memberRepository.delete(member);
    }

    public void changeNickName(MemberRequest.changeNickNameRequestDTO nickNameRequestDTO) {
        Member member;
        try {
            member = findMember();
        } catch (Exception404 e) {
            log.error("Access Token에서 뽑아낸 회원이 존재하지 않는 회원입니다. (전체 닉네임 변경)");
            throw e;
        }
        member.changeNickName(nickNameRequestDTO.getNewNickName());
    }

    public void changePassword(MemberRequest.changePasswordRequestDTO changePasswordDTO) {
        Member member;
        try {
            member = findMember();
        } catch (Exception404 e) {
            log.error("Access Token에서 뽑아낸 회원이 존재하지 않는 회원입니다. (비밀번호 변경)");
            throw e;
        }
        if(!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), member.getPassword())) {
            log.error("비밀번호 변경 요청에서의 비밀번호 확인이 틀렸습니다. User Id : " + member.getEmail());
            throw new Exception400("비밀번호가 틀렸습니다.");
        }
        member.changePassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
    }

    public void sendEmail(String email) {
        Member member;
        try {
            member = findMember();
        } catch (Exception404 e) {
            log.error("Access Token에서 뽑아낸 회원이 존재하지 않는 회원입니다. (부산대 인증 메일 전송)");
            throw e;
        }
        Integer authNumber = makeEmailAuthNum();
        redisUtility.setValues(email, Integer.toString(authNumber), 300);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("부산대 인증 메일입니다!");
        simpleMailMessage.setText("인증 번호 입니다.\n"
                + authNumber +
                "\n잘 입력해 보세요!");
        // 이메일 발신
        javaMailSender.send(simpleMailMessage);
    }

    public void checkPNUEmail(MemberRequest.checkPNUEmailRequestDTO pnuEmailRequestDTO) {
        Member member;
        try {
            member = findMember();
        } catch (Exception404 e) {
            log.error("Access Token에서 뽑아낸 회원이 존재하지 않는 회원입니다. (부산대 인증 메일 확인)");
            throw e;
        }
        if(!redisUtility.getValues(pnuEmailRequestDTO.getEmail()).equals(pnuEmailRequestDTO.getCertificationNumber())){
            log.error("부산대 메일 인증 번호가 틀렸습니다. User Id : " + member.getEmail());
            throw new Exception400("인증번호가 틀렸습니다.");
        }
    }

    public void findPassword(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isEmpty()) {
            log.error("가입된 회원이 존재하지 않는 이메일입니다. (비밀번호 찾기)");
            throw new Exception404("없는 회원입니다.");
        }
        String randomPassword = makeRandomPassword();
        member.get().changePassword(passwordEncoder.encode(randomPassword));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("비밀번호가 변경되었습니다.");
        simpleMailMessage.setText("임의 생성된 비밀번호입니다.\n"
                + randomPassword +
                "\n추후에 꼭 변경하세요!");
        // 이메일 발신
        javaMailSender.send(simpleMailMessage);
    }

    public Integer makeEmailAuthNum() {
        return new Random().nextInt(888888) + 111111;
    }

    public String makeRandomPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    private Member findMember() {
        Optional<Member> member = memberRepository.findById(currentMember());
        if(member.isEmpty()) {
            throw new Exception404("없는 회원입니다.");
        }
        return member.get();
    }

    public void getKakaoInfo(String code) {

        String accessToken = "";

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            MemberResponse.KakaoTokenDTO kakaoTokenDTO = objectMapper.readValue(response.getBody(), MemberResponse.KakaoTokenDTO.class);
            accessToken = kakaoTokenDTO.getAccess_token();

        }  catch (JsonProcessingException e) {
            throw new Exception500("Json 파싱 에러입니다." + e);
        }
        getUserInfoWithToken(accessToken);
    }

    private void getUserInfoWithToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        System.out.println(response.getBody());

        /*ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readValue(response.getBody());

        } catch (JsonProcessingException e) {
            throw new Exception500("Json 파싱 에러입니다." + e);
        }*/
    }
}