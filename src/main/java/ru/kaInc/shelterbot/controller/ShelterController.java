package ru.kaInc.shelterbot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kaInc.shelterbot.service.ShelterService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shelter")
@Tag(name = "Приюты", description = "Операции с приютами")
public class ShelterController {

    private final ShelterService shelterService;
}
