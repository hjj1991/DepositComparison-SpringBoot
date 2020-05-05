package com.myhome.service.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.config.security.JwtTokenProvider;
import com.myhome.domain.user.UserEntity;
import com.myhome.domain.user.UserRepository;
import com.myhome.domain.user.dto.UserDetailResponseDto;
import com.myhome.domain.user.dto.UserSignRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(String.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException());
    }

    public UserDetailResponseDto getUserDetail(UserEntity userEntity){
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        objectMapper.readValue(EntityUtils.toString(operationResponse.getEntity()), WorkloadOperationDTO.class);
        ModelMapper modelMapper = new ModelMapper();

        UserDetailResponseDto userDetailResponseDto = modelMapper.map(userEntity, UserDetailResponseDto.class);

        return userDetailResponseDto;

    }


}
