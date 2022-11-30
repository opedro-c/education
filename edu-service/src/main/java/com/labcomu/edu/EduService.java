package com.labcomu.edu;

import com.labcomu.edu.client.OrcidGateway;
import com.labcomu.edu.client.OrgGateway;
import com.labcomu.edu.resource.Organization;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;


@Service
@Validated
@RequiredArgsConstructor
public class EduService {
    private final OrgGateway orgGateway;
    private final OrcidGateway orcidGateway;

    public Organization getOrganization(String url) {
        try {
            Organization organization = orgGateway.getOrganization(url);
            organization.setResearchers(organization.getResearchers().stream().map(researcher -> orcidGateway.getResearcher(researcher.getOrcid())).toList());
            return organization;
        } catch (Throwable e) {
            e.printStackTrace();
            LogWriter.writeLog(e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Organization Not Found", e);
        }
    }
}
