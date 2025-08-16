package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.Exportable;
import carevn.luv2code.cms.tevc_cms_api.exception.ExcelExportException;
import carevn.luv2code.cms.tevc_cms_api.repository.DepartmentRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PayrollRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.StudentRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.ExcelExportService;
import carevn.luv2code.cms.tevc_cms_api.util.ExcelColumn;
import carevn.luv2code.cms.tevc_cms_api.util.ExcelGenerator;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PayrollRepository payrollRepository;

    public ExcelExportServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            PayrollRepository payrollRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.payrollRepository = payrollRepository;
    }

    @Override
    public ByteArrayInputStream exportToExcel(String entityType) {
        Exportable exportable = getExportableEntity(entityType);
        ExcelGenerator generator = new ExcelGenerator(exportable.getExcelColumns(), exportable.getData(), entityType);
        return generator.generate();
    }

    private Exportable getExportableEntity(String entityType) {
        switch (entityType.toLowerCase()) {
            case "student":
                return getStudentExportable();
            case "user":
                return getUserExportable();
            case "employee":
                return getEmployeeExportable();
            case "department":
                return getDepartmentExportable();
            case "payroll":
                return getPayrollExportable();
            default:
                throw new ExcelExportException("Unsupported entity type: " + entityType);
        }
    }

    private Exportable getStudentExportable() {
        return new Exportable() {
            @Override
            public List<ExcelColumn> getExcelColumns() {
                return List.of(
                        new ExcelColumn("STT", "stt", Integer.class),
                        new ExcelColumn("ID", "id", UUID.class),
                        new ExcelColumn("Name", "name", String.class),
                        new ExcelColumn("Email", "email", String.class),
                        new ExcelColumn("Mobile No.", "mobileNo", String.class));
            }

            @Override
            public List<?> getData() {
                return studentRepository.findAll();
            }
        };
    }

    private Exportable getUserExportable() {
        return new Exportable() {
            @Override
            public List<ExcelColumn> getExcelColumns() {
                return List.of(
                        new ExcelColumn("STT", "stt", Integer.class),
                        new ExcelColumn("ID", "id", UUID.class),
                        new ExcelColumn("UserName", "userName", String.class),
                        new ExcelColumn("Email", "email", String.class),
                        new ExcelColumn("Address", "address", String.class),
                        new ExcelColumn("Phone Number", "phoneNumber", String.class),
                        new ExcelColumn("Enabled", "enabled", Boolean.class));
            }

            @Override
            public List<?> getData() {
                return userRepository.findAll();
            }
        };
    }

    private Exportable getEmployeeExportable() {
        return new Exportable() {
            @Override
            public List<ExcelColumn> getExcelColumns() {
                return List.of(
                        new ExcelColumn("STT", "stt", Integer.class),
                        new ExcelColumn("ID", "id", UUID.class),
                        new ExcelColumn("Employee Code", "employeeCode", String.class),
                        new ExcelColumn("First Name", "firstName", String.class),
                        new ExcelColumn("Last Name", "lastName", String.class),
                        new ExcelColumn("Date of Birth", "dateOfBirth", Date.class),
                        new ExcelColumn("Gender", "gender", String.class),
                        new ExcelColumn("Email", "email", String.class),
                        new ExcelColumn("Phone", "phone", String.class),
                        new ExcelColumn("Address", "address", String.class),
                        new ExcelColumn("Hire Date", "hireDate", Date.class),
                        new ExcelColumn("Is Active", "isActive", Boolean.class));
            }

            @Override
            public List<?> getData() {
                return employeeRepository.findAll();
            }
        };
    }

    private Exportable getDepartmentExportable() {
        return new Exportable() {
            @Override
            public List<ExcelColumn> getExcelColumns() {
                return List.of(
                        new ExcelColumn("STT", "stt", Integer.class),
                        new ExcelColumn("ID", "id", UUID.class),
                        new ExcelColumn("Name", "name", String.class),
                        new ExcelColumn("Description", "description", String.class),
                        new ExcelColumn("Manager Name", "managerName", String.class));
            }

            @Override
            public List<?> getData() {
                return departmentRepository.findAll().stream()
                        .map(department -> new Object() {
                            public UUID id = department.getId();
                            public String name = department.getName();
                            public String description = department.getDescription();
                            public String managerName = department.getManager() != null
                                    ? department.getManager().getFirstName() + " "
                                            + department.getManager().getLastName()
                                    : "";
                        })
                        .toList();
            }
        };
    }

    private Exportable getPayrollExportable() {
        return new Exportable() {
            @Override
            public List<ExcelColumn> getExcelColumns() {
                return List.of(
                        new ExcelColumn("STT", "stt", Integer.class),
                        new ExcelColumn("ID", "id", UUID.class),
                        new ExcelColumn("Employee Name", "employeeName", String.class),
                        new ExcelColumn("Period", "period", String.class),
                        new ExcelColumn("Basic Salary", "basicSalary", Double.class),
                        new ExcelColumn("Overtime", "overtime", Double.class),
                        new ExcelColumn("Bonus", "bonus", Double.class),
                        new ExcelColumn("Allowances", "allowances", Double.class),
                        new ExcelColumn("Deductions", "deductions", Double.class),
                        new ExcelColumn("Tax", "tax", Double.class),
                        new ExcelColumn("Insurance", "insurance", Double.class),
                        new ExcelColumn("Net Salary", "netSalary", Double.class),
                        new ExcelColumn("Status", "status", String.class),
                        new ExcelColumn("Processed Date", "processedDate", Date.class),
                        new ExcelColumn("Paid Date", "paidDate", Date.class));
            }

            @Override
            public List<?> getData() {
                return payrollRepository.findAll().stream()
                        .map(payroll -> new Object() {
                            public UUID id = payroll.getId();
                            public String employeeName = payroll.getEmployee() != null
                                    ? payroll.getEmployee().getFirstName() + " "
                                            + payroll.getEmployee().getLastName()
                                    : "";
                            public String period = payroll.getPeriod();
                            public Double basicSalary = payroll.getBasicSalary();
                            public Double overtime = payroll.getOvertime();
                            public Double bonus = payroll.getBonus();
                            public Double allowances = payroll.getAllowances();
                            public Double deductions = payroll.getDeductions();
                            public Double tax = payroll.getTax();
                            public Double insurance = payroll.getInsurance();
                            public Double netSalary = payroll.getNetSalary();
                            public String status = payroll.getStatus() != null
                                    ? payroll.getStatus().toString()
                                    : "";
                            public Date processedDate = payroll.getProcessedDate();
                            public Date paidDate = payroll.getPaidDate();
                        })
                        .toList();
            }
        };
    }
}
