//package com.recruit.system.mapper;
//
//import com.recruit.system.dto.response.DocumentResponse;
//import com.recruit.system.model.Document;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DocumentMapper {
//
//    public DocumentResponse toResponse(Document document) {
//        if (document == null) {
//            return null;
//        }
//
//        DocumentResponse response = new DocumentResponse();
//
//        response.setId(document.getId());
//        response.setFileName(document.getFileName());
//        response.setUploadedAt(document.getUploadedAt());
//
//        return response;
//    }
//}
package com.recruit.system.mapper;

import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(Document document) {
        if (document == null) {
            return null;
        }

        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        response.setFileName(document.getFileName());
        response.setUploadedAt(document.getUploadedAt());
        return response;
    }
}