package mainPackage.announcementPackage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {

    Announcement findAnnouncementById(int id);
}
