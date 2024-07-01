package you_tube_own.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.repository.ProfileRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByEmailAndVisibleTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with email " + username));

        return new CustomUserDetail(profile);
    }
}

