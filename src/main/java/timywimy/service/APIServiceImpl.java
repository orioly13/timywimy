package timywimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timywimy.model.security.User;
import timywimy.repository.APIRepository;
import timywimy.util.StringUtil;

import java.util.UUID;

@Service
public class APIServiceImpl implements APIService {

    private final APIRepository apiRepository;

    @Autowired
    public APIServiceImpl(APIRepository apiRepository) {
        this.apiRepository = apiRepository;

    }

    @Override
    public UUID register(timywimy.web.dto.User userDTO) {
        if (userDTO == null ||
                StringUtil.isEmpty(userDTO.getEmail()) ||
                StringUtil.isEmpty(userDTO.getPassword()) ||
                StringUtil.isEmpty(userDTO.getName())) {
            throw new RuntimeException("user with email, pass and name should be provided");
        }
        return apiRepository.register(userDTO);
    }

    @Override
    public UUID openSession(timywimy.web.dto.User userDTO) {
        if (userDTO == null ||
                StringUtil.isEmpty(userDTO.getEmail()) ||
                StringUtil.isEmpty(userDTO.getPassword())) {
            throw new RuntimeException("user with email,pass should be provided");
        }
        return apiRepository.openSession(userDTO);
    }

    @Override
    public boolean closeSession(UUID sessionId) {
        if (sessionId == null) {
            throw new RuntimeException("session should be provided");
        }
        return apiRepository.closeSession(sessionId);
    }

    @Override
    public User getUserBySession(UUID sessionId) {
        if (sessionId == null) {
            throw new RuntimeException("session should be provided");
        }
        return apiRepository.getUserBySession(sessionId);
    }
}
