package com.vermau2k01.notes_application.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String noteId;
    @Lob
    private String content;
    private String ownerUserName;
}
