package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.TeamDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Team;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.TeamMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.DepartmentRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.TeamRepository;
import carevn.luv2code.cms.tevc_cms_api.service.TeamService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TeamMapper teamMapper;

    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);

        team.setDepartment(departmentRepository
                .findById(teamDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND)));

        Set<Employee> employees = new HashSet<>(employeeRepository.findAllById(teamDTO.getEmployeeIds()));
        team.setEmployees(employees);

        return teamMapper.toDTO(teamRepository.save(team));
    }

    @Override
    public TeamDTO updateTeam(Integer id, TeamDTO teamDTO) {
        Team existingTeam = teamRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));

        teamMapper.updateEntityFromDTO(teamDTO, existingTeam);

        if (teamDTO.getDepartmentId() != null) {
            existingTeam.setDepartment(departmentRepository
                    .findById(teamDTO.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND)));
        }

        if (teamDTO.getEmployeeIds() != null) {
            Set<Employee> employees = new HashSet<>(employeeRepository.findAllById(teamDTO.getEmployeeIds()));
            existingTeam.setEmployees(employees);
        }

        return teamMapper.toDTO(teamRepository.save(existingTeam));
    }

    @Override
    public TeamDTO getTeamById(Integer id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));
        return teamMapper.toDTO(team);
    }

    @Override
    public Page<TeamDTO> getAllTeams(int page, int size) {
        Page<Team> salaryPage = teamRepository.findAll(PageRequest.of(page, size));
        return salaryPage.map(teamMapper::toDTO);
    }

    @Override
    public List<TeamDTO> getAllTeamsNoPaging() {
        return teamRepository.findAll().stream().map(teamMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteTeam(Integer id) {
        if (!teamRepository.existsById(id)) {
            throw new AppException(ErrorCode.TEAM_NOT_FOUND);
        }
        teamRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TeamDTO addMember(Integer teamId, Integer employeeId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (team.getEmployees().contains(employee)) {
            throw new AppException(ErrorCode.EMPLOYEE_ALREADY_IN_TEAM);
        }

        team.getEmployees().add(employee);
        return teamMapper.toDTO(teamRepository.save(team));
    }

    @Override
    @Transactional
    public TeamDTO removeMember(Integer teamId, Integer employeeId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        team.getEmployees().remove(employee);
        return teamMapper.toDTO(teamRepository.save(team));
    }
}
