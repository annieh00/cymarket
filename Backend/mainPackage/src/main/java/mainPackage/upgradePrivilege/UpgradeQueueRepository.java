package mainPackage.upgradePrivilege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpgradeQueueRepository extends JpaRepository<UpgradeQueue,Long> {
    UpgradeQueue findUpgradeQueueByUserName(String userName);

}
