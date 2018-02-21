package timywimy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.entities.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class APIRepositoryImpl implements APIRepository {

    private Map<UUID, User> sessions;
    private final UserRepository userRepository;
    private final User apiUser;

    @Autowired
    public APIRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.sessions = new HashMap<>();
        this.apiUser = userRepository.get(UUID.fromString("3088b1fc-43c2-4951-8b78-1f56261c16ca"));

    }

    @Override
    public UUID register(timywimy.web.dto.User userDTO) {
//        if (userDTO == null ||
//                StringUtil.isEmpty(userDTO.getEmail()) ||
//                StringUtil.isEmpty(userDTO.getPassword()) ||
//                StringUtil.isEmpty(userDTO.getName())) {
//            throw new RuntimeException("user with email,pass and name should be provided");
//        }
        if (userRepository.getByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("user already registered");
        }
        User saved = userRepository.save(createFromUserDTO(userDTO), apiUser.getId());
        if (saved == null) {
            throw new RuntimeException("failed to register user");
        }
        UUID session = UUID.randomUUID();
        sessions.put(session, saved);
        return session;
    }

    @Override
    public UUID openSession(timywimy.web.dto.User userDTO) {
//        if (userDTO == null ||
//                StringUtil.isEmpty(userDTO.getEmail()) ||
//                StringUtil.isEmpty(userDTO.getPassword())) {
//            throw new RuntimeException("user with email,pass should be provided");
//        }
        User byEmail = userRepository.getByEmail(userDTO.getEmail());
        if (byEmail == null || !byEmail.getPassword().equals(userDTO.getPassword())) {
            throw new RuntimeException("user with email and pass not found");
        }
        UUID session = UUID.randomUUID();
        sessions.put(session, byEmail);
        return session;
    }

    @Override
    public boolean closeSession(UUID sessionId) {
//        if (sessionId == null) {
//            throw new RuntimeException("session should be provided");
//        }
        sessions.remove(sessionId);
        return true;
    }

    @Override
    public User getUserBySession(UUID sessionId) {
//        if (sessionId == null) {
//            throw new RuntimeException("session should be provided");
//        }
        User user = sessions.get(sessionId);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        return user;
    }

    private User createFromUserDTO(timywimy.web.dto.User dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setRole(Role.USER);
        return user;
    }
}
