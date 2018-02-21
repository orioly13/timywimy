package timywimy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import timywimy.model.security.UserImpl;
import timywimy.model.security.converters.Role;
import timywimy.repository.entities.UserRepository;
import timywimy.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class APIRepositoryImpl implements APIRepository {

    private Map<UUID, UserImpl> sessions;
    private final UserRepository userRepository;
    private final UserImpl apiUser;

    @Autowired
    public APIRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.sessions = new HashMap<>();
        this.apiUser = userRepository.get(UUID.fromString("3fca3433-9dbb-4654-b0fb-2b6abfea72ff"));

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
        UserImpl saved = userRepository.save(createFromUserDTO(userDTO), apiUser.getId());
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
        UserImpl byEmail = userRepository.getByEmail(userDTO.getEmail());
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
    public UserImpl getUserBySession(UUID sessionId) {
//        if (sessionId == null) {
//            throw new RuntimeException("session should be provided");
//        }
        UserImpl user = sessions.get(sessionId);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        return user;
    }

    private UserImpl createFromUserDTO(timywimy.web.dto.User dto) {
        UserImpl user = new UserImpl();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(Role.USER);
        return user;
    }
}
