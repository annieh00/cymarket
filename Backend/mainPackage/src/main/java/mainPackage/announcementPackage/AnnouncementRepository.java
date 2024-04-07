package mainPackage.announcementPackage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    public Announcement findAnnouncementById(int id);
}
