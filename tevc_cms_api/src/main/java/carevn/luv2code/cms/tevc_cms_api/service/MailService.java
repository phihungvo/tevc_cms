package carevn.luv2code.cms.tevc_cms_api.service;

import org.springframework.web.multipart.MultipartFile;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.MailRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.TemplateMailRequest;

public interface MailService {
    void send(MailRequest request);

    void sendWithAttachments(MailRequest request, MultipartFile[] attachments);

    void sendTemplate(TemplateMailRequest request);

    void sendTemplateWithAttachments(TemplateMailRequest request, MultipartFile[] attachments);
}
