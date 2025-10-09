package carevn.luv2code.cms.tevc_cms_api.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartData {
    private List<String> categories = new ArrayList<>();
    private List<Series> series = new ArrayList<>();
}
