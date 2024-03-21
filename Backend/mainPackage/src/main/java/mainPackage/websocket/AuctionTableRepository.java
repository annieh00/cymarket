package mainPackage.websocket;


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
   public AuctionTable getAuctionTableById(String id);

}
