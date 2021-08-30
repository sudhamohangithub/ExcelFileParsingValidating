package com.example.springboot.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRecord {
    private int rowNumber;

    @NotEmpty(message = "{nickName.not.empty}")
    @Size(max = 50, message = "{nickName.size.max}")
    private String nickName;

    @Size(max = 50, message = "{firstName.size.max}")
    @NotEmpty(message = "{firstName.not.empty}")
    private String firstName;

    @Size(max = 50, message = "{middleName.size.max}")
    private String middleName;

    @Size(max = 50, message = "{lastName.size.max}")
    @NotEmpty(message = "{lastName.not.empty}")
    private String lastName;

    @Size(max = 12, message = "{mobileNumber.size.max}")
    private String mobileNumber;

    @Size(max = 50, message = "{emailId.size.max}")
    private String emailId;

    private LocalDate birthDate;

    @Size(max = 50, message = "{street.size.max}")
    @NotEmpty(message = "{street.not.empty}")
    private String street;

    private Long houseNumber;

    @Size(max = 6, message = "{zipCode.size.max}")
    @NotEmpty(message = "{zipCode.not.empty}")
    private String zipCode;

    @Size(max = 50, message = "{city.size.max}")
    @NotEmpty(message = "{city.not.empty}")
    private String city;


    private String field1;
    private String field2;
    private String field3;

}
