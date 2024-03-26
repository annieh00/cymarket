package mainPackage.websocket;


import jakarta.transaction.Transactional;
import mainPackage.usersPackage.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author Junhyung Shim
 * */
@Repository
public interface AuctionTableRepository extends JpaRepository<AuctionTable,Long> {
   @Transactional
   AuctionTable getAuctionTableById(String id);
   
   AuctionTable getAuctionTableByPost(Posting p);

}
