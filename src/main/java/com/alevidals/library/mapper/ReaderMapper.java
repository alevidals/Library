package com.alevidals.library.mapper;

import com.alevidals.library.dto.ReaderDto;
import com.alevidals.library.model.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "")
public interface ReaderMapper {

    ReaderMapper MAPPER = Mappers.getMapper(ReaderMapper.class);

    ReaderDto readerToReaderDto(Reader reader);

    Reader readerDtoToReader(ReaderDto readerDto);
}
