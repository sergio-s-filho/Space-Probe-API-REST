package com.example.sonda.controller;

import com.example.sonda.model.SondaModel;
import com.example.sonda.repository.SondaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sonda")
public class SondaController {
    @Autowired
    SondaRepository sondaRepository;


    // EndPoint POST() --> adiciona uma nova sonda em nosso DataBase

    @PostMapping("/")
    public ResponseEntity<Object> adicionaSonda(@RequestBody SondaModel sondaModelJson){
        SondaModel sondaModelTemp = new SondaModel();
        sondaModelTemp.setPosX(sondaModelJson.getPosX());
        sondaModelTemp.setPosY(sondaModelJson.getPosY());
        sondaModelTemp.setOrientacao(sondaModelJson.getOrientacao());
        return ResponseEntity.status(HttpStatus.CREATED).body(sondaRepository.save(sondaModelTemp));
    }


    // EndPoint GET() --> retorna uma lista com todas as sondas cadastradas no DataBase
    @GetMapping("/")
    public ResponseEntity<List> mostrarSondas(){
        return ResponseEntity.status(HttpStatus.OK).body(sondaRepository.findAll());
    }

    //EndPoint GET() param: {id} --> retorna uma sonda de acordo com o Id passado como parametro
    @GetMapping("/{id}")
    public ResponseEntity<Object> encontrarSonda(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(sondaRepository.findById(id).get());
    }

    //EndPoint PUT() param: {id} --> encontra a sonda pelo Id e atualiza seus dados

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarDadosSonda(@PathVariable UUID id, @RequestBody SondaModel sonda){
        SondaModel sondaModelTemp = sondaRepository.findById(id).get();
        sondaModelTemp.setPosX(sonda.getPosX());
        sondaModelTemp.setPosY(sonda.getPosY());
        sondaModelTemp.setOrientacao(sonda.getOrientacao());

        return ResponseEntity.status(HttpStatus.OK).body(sondaRepository.save(sondaModelTemp));
    }

    //EndPoint GET() param: {id} param: {comando} --> busca uma sonda no DataBase pelo ID e invoca o metodo
    //followCommands, que passado uma String de comando, a sonda realiza sua movimentação, retorna os dados
    //atualizados na sonda original


    @GetMapping("/{id}/{comando}")
    public ResponseEntity<Object> enviarComandosSonda(@PathVariable UUID id, @PathVariable String comando){
        SondaModel sondaModelTemp = sondaRepository.findById(id).get();
        sondaModelTemp.followCommands(comando);
        return ResponseEntity.status(HttpStatus.OK).body(sondaRepository.save(sondaModelTemp));
    }
    //EndPoint DELETE() param: {id} -> remove uma sonda de acordo com o seu Id

   @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarSonda(@PathVariable UUID id){
        SondaModel sondaModelTemp = sondaRepository.findById(id).get();
        sondaRepository.delete(sondaModelTemp);
        return ResponseEntity.status(HttpStatus.OK).body("Deletado");
   }
}