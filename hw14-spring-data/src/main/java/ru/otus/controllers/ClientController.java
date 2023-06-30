package ru.otus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientDto;
import ru.otus.entity.Address;
import ru.otus.entity.Client;
import ru.otus.entity.Phone;
import ru.otus.services.ClientService;

import java.util.Collections;
import java.util.List;


@Controller
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/")
    public String index(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "index";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new ClientDto());
        return "client_create";
    }

    @PostMapping("/client/save")
    public RedirectView add_client(ClientDto clientDto) {
        clientService.save(new Client(
                clientDto.getName(),
                new Address(clientDto.getAddress()),
                Collections.singletonList(new Phone(clientDto.getPhone())))
        );
        return new RedirectView("/", true);
    }

}
