package ru.kishko.photoservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kishko.photoservice.entities.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {

}
