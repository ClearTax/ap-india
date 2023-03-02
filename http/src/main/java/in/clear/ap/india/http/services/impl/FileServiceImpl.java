package in.clear.ap.india.http.services.impl;

import in.clear.ap.india.http.dtos.request.FileStatusUpdateDTO;
import in.clear.ap.india.http.dtos.request.FileStatusUpdateRequest;
import in.clear.ap.india.http.models.File;
import in.clear.ap.india.http.repository.FileRepository;
import in.clear.ap.india.http.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public void updateFileStatus(FileStatusUpdateRequest fileStatusUpdateRequest) {
        Map<String,FileStatusUpdateDTO> fileStatusUpdateDTOMap = fileStatusUpdateRequest.getFileStatusUpdateDTOList().stream().collect(Collectors.toMap(FileStatusUpdateDTO::getFileId, el->el));
        List<File> files = fileRepository.findByActivity_IdAndIdIn(fileStatusUpdateRequest.getActivityId(), new ArrayList<>(fileStatusUpdateDTOMap.keySet()));
        for(File file: files){
            FileStatusUpdateDTO fileStatusUpdateDTO = fileStatusUpdateDTOMap.get(file.getId());
            file.setFileStatus(fileStatusUpdateDTO.getFileStatus());
            file.setOcrId(fileStatusUpdateDTO.getOcrId());
            file.setErrorMessage(fileStatusUpdateDTO.getErrorMessage());
        }
        fileRepository.saveAll(files);
    }
}
