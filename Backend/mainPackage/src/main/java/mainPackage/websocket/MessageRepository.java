package mainPackage.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    List<Message> findByUserSentUserNameAndUserReceivedUserNameOrUserSentUserNameAndUserReceivedUserNameOrderBySent(String user1, String user2, String user3, String user4);

}

