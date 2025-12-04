package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http://example.com", maxAge = 3600)
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec, Pageable pageable){
       Page<UserModel> userModelPage = userService.findAll(spec,pageable);

        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
        //Utilizando o userModels, porém necessita da declaração do List.

        //List<UserModel> userModels = userService.findAll();
        //return ResponseEntity.status(HttpStatus.OK).body(userModels);

        //Utilizando direto pelo userService, não tem a necessidade da declaração do List.
        //return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());


    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        //Retornando utilizando o optionalUserModel
        //Optional<UserModel> optionalUserModel = userService.findById(userId);
        //return ResponseEntity.status(HttpStatus.OK).body(optionalUserModel.get());

        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        userService.delete(userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");

    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId")UUID userId,
                                             @RequestBody
                                             // Valida apenas os campos do grupo UserPut, permitindo regras de validação específicas
                                            // para a operação de atualização de nome e telefone. (Validação por grupos)
                                             @Validated(UserRecordDTO.UserView.UserPut.class)
                                             @JsonView(UserRecordDTO.UserView.UserPut.class)
                                             UserRecordDTO userRecordDTO){
        //var userModel = userService.findById(userId).get();
        //userService.updateUser(userRecordDTO, userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDTO, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId")UUID userId,
                                             @RequestBody
                                             // Valida apenas os campos do grupo PasswordPut, permitindo regras de validação específicas
                                             // para a operação de atualização de senha. (Validação por grupos)
                                             @Validated(UserRecordDTO.UserView.PasswordPut.class)
                                             @JsonView(UserRecordDTO.UserView.PasswordPut.class)
                                             UserRecordDTO userRecordDTO){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.get().getPassword().equals(userRecordDTO.oldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }
        userService.updatePassword(userRecordDTO, userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated sucessfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImages(@PathVariable(value = "userId")UUID userId,
                                             @RequestBody
                                             // Valida apenas os campos do grupo ImagePut, permitindo regras de validação específicas
                                             // para a operação de atualização de imagem. (Validação por grupos)
                                             @Validated(UserRecordDTO.UserView.ImagePut.class)
                                             @JsonView(UserRecordDTO.UserView.ImagePut.class)
                                             UserRecordDTO userRecordDTO){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userRecordDTO, userService.findById(userId).get()));
    }

}
