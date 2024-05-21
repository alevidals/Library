package com.alevidals.library.service.impl;

import com.alevidals.library.model.Reader;
import com.alevidals.library.repository.ReaderRepository;
import com.alevidals.library.service.ReaderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class ReaderServiceImpl implements ReaderService {

    private ReaderRepository readerRepository;

    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @Override
    public List<Reader> findAll() {
        return StreamSupport.stream(
                readerRepository.findAll().spliterator(), false
        ).toList();
    }

    @Override
    public Reader save(Reader reader) {
        return readerRepository.save(reader);
    }

    @Override
    public Reader save(UUID id, Reader reader) {
        reader.setId(id);
        return readerRepository.save(reader);
    }

    @Override
    public Optional<Reader> findById(UUID id) {
        return readerRepository.findById(id);
    }

    @Override
    public boolean exists(UUID id) {
        return readerRepository.existsById(id);
    }

    @Override
    public void delete(UUID id) {
        readerRepository.deleteById(id);
    }

    @Override
    public Reader partialUpdate(UUID id, Reader reader) {
        reader.setId(id);

        return readerRepository.findById(id).map(existingReader -> {
            Optional.ofNullable(reader.getDocument()).ifPresent(existingReader::setDocument);
            Optional.ofNullable(reader.getName()).ifPresent(existingReader::setName);
            Optional.ofNullable(reader.getSurnames()).ifPresent(existingReader::setSurnames);

            return readerRepository.save(existingReader);
        }).orElseThrow(() -> new RuntimeException("Reader not found"));
    }
}
