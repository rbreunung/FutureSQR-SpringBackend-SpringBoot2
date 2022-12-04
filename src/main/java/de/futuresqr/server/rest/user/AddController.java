/**
 * MIT License
 *
 * Copyright (c) 2022 Robert Breunung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.futuresqr.server.rest.user;

import static org.springframework.util.Assert.hasText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import de.futuresqr.server.model.backend.PersistenceUser;
import de.futuresqr.server.model.frontend.SimpleUser;
import de.futuresqr.server.persistence.UserRepository;
import de.futuresqr.server.service.user.UserUuidGenerator;

/**
 * Create new user.
 */
@RestController
public class AddController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserUuidGenerator generator;

	@PostMapping("/rest/user/add")
	SimpleUser postUserAdd(@RequestPart("userName") String loginName, @RequestPart("displayName") String displayName,
			@RequestPart("contactEmail") String email, @RequestPart("password") String password) {

		hasText(email, "Email required.");
		hasText(displayName, "Display name required.");
		hasText(loginName, "Login name required.");
		hasText(password, "Password required");

		PersistenceUser user = PersistenceUser.builder().uuid(generator.getUuid(loginName)).displayName(displayName)
				.loginName(loginName).email(email).password(encoder.encode(password)).build();
		user = userRepository.save(user);

		return SimpleUser.from(user);
	}
}
