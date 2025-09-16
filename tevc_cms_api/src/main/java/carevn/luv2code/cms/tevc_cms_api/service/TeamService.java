package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.TeamDTO;

public interface TeamService {
    TeamDTO createTeam(TeamDTO teamDTO);

    TeamDTO updateTeam(Integer id, TeamDTO teamDTO);

    TeamDTO getTeamById(Integer id);

    Page<TeamDTO> getAllTeams(int page, int size);

    List<TeamDTO> getAllTeamsNoPaging();

    void deleteTeam(Integer id);

    TeamDTO addMember(Integer teamId, Integer employeeId);

    TeamDTO removeMember(Integer teamId, Integer employeeId);
}
