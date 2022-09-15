package backend.eventa.controllers;


import backend.eventa.models.User;
import backend.eventa.payload.request.ChangePasswordRequest;
import backend.eventa.payload.request.LoginRequest;
import backend.eventa.payload.request.SignupRequest;
import backend.eventa.payload.response.JwtResponse;
import backend.eventa.payload.response.MessageResponse;
import backend.eventa.repository.UserRepository;
import backend.eventa.security.jwt.JwtUtils;
import backend.eventa.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The controller that stores the implementation of all authorization-related queries.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	/**
	 * Login query. Checks that the user's credentials are valid and returns him a JWT.
	 * @param loginRequest
	 * @return JwtResponse
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail()
		));
	}

	/**
	 * Registration request. Checking if the user's data is unique and creating the appropriate hash of his password.
	 * @param signUpRequest
	 * @return MessageResponse
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	/**
	 * Supports changing user password. Request requiring JWT.
	 * @param changeRequest
	 * @param authentication
	 * @return MessageResponse
	 */
	@PutMapping("/changepassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changeRequest, Authentication authentication) {
		Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

		User user = userRepository.getById(user_id);


		user.setPassword(encoder.encode(changeRequest.getPassword()));
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Change password successfully!"));
	}
}
