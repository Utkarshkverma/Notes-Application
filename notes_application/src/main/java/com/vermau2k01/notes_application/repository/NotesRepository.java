package com.vermau2k01.notes_application.repository;

import com.vermau2k01.notes_application.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, String> {

    List<Notes> findByOwnerUserName(String name);
}
