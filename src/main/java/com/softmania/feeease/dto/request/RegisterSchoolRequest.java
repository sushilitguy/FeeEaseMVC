package com.softmania.feeease.dto.request;

public record RegisterSchoolRequest(String schoolName, String address, String contactNo, String email, String adminUserName, String adminPassword) {
}
