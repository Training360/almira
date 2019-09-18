package crystalball.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class FileResult {

    private String filename;

    private Resource resource;

    private String contentType;
}
