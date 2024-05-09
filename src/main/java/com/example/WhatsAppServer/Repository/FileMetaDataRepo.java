package com.example.WhatsAppServer.Repository;

import com.example.WhatsAppServer.Entity.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepo extends JpaRepository<FileMetaData,Integer> {
}
