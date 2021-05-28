package ua.project.forit.data.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.project.forit.data.entities.models.Confirm;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.repositories.ConfirmRepository;
import ua.project.forit.exceptions.ConfirmServiceException;
import ua.project.forit.exceptions.UserServiceException;
import ua.project.forit.services.MailSenderService;

import java.util.Random;
import java.util.UUID;

@Service
public class ConfirmService
{
    protected final ConfirmRepository confirmRepository;
    protected final MailSenderService mailSenderService;
    protected final UserService userService;
    protected final PasswordEncoder passwordEncoder;
    protected final AuthenticationTokenService authenticationTokenService;
    protected final Random random;
    protected final int minRandomNum;
    protected final int maxRandomNum;

    @Autowired
    public ConfirmService(ConfirmRepository confirmRepository,
                          MailSenderService mailSenderService,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationTokenService authenticationTokenService)
    {
        this.confirmRepository = confirmRepository;
        this.mailSenderService = mailSenderService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationTokenService = authenticationTokenService;
        random = new Random();
        minRandomNum = 1000;
        maxRandomNum = 9999;
    }


    public String saveUserRegistrationRequest(@NonNull Confirm confirm, @NonNull String IP) throws ConfirmServiceException
    {
        if(userService.userExistsByEmail(confirm.getEmail()))
            throw new ConfirmServiceException("Вже існує акаунт з даною електронною адресою");

        confirm.setConfirmHash(UUID.randomUUID().toString());
        confirm.setConfirmCode(Integer.toString(random.nextInt(maxRandomNum - minRandomNum) + minRandomNum));
        confirm.setRegisterIP(IP);
        confirm.setPassword(passwordEncoder.encode(confirm.getPassword()));
        confirmRepository.save(confirm);

        mailSenderService.send(confirm.getEmail(), "Код підтвердження", "Код підтвердження: " + confirm.getConfirmCode());

        return confirm.getConfirmHash();
    }

    public String confirmUserRegistrationEmail(@NonNull String hash, @NonNull String code) throws ConfirmServiceException
    {
        Confirm confirm = confirmRepository.getByConfirmHash(hash);

        if(confirm == null || !confirm.isCodeCorrect(code))
            throw new ConfirmServiceException("Невірний код підтвердження.");
        else if (userService.userExistsByEmail(confirm.getEmail()))
        {
            confirmRepository.removeConfirmByEmail(confirm.getEmail());
            throw new ConfirmServiceException("Вже існує акаунт з даною електронною адресою");
        }

        userService.createUser(new User(confirm));
        confirmRepository.removeConfirmByEmail(confirm.getEmail());

//        try
//        {
//            user = userService.getUserByEmail(user.getEmail());
//        }
//        catch (UserServiceException ignore)
//        {}
        return null;
        //return authenticationTokenService.generateToken(user, confirm.getRegisterIP(), "false").toString();
    }
}
