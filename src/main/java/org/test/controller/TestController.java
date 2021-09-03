package org.test.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class TestController {

	@GetMapping(path="/whoami")
	@Operation(summary = "Retrieve information about the authenticated user")
	public String whoami(@AuthenticationPrincipal UserDetails user) {
	  return "Your user name is " + user.getUsername();
  }

}