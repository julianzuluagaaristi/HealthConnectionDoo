import { Component, OnInit } from '@angular/core';
import { AgendaService } from './agenda.service';
import { Router } from '@angular/router';
import { Agenda } from './agenda';

@Component({
  selector: 'app-formagenda',
  templateUrl: './formagenda.component.html',
  styleUrls: ['./formagenda.component.css']
})
export class FormagendaComponent implements OnInit{

  agenda:Agenda=new Agenda();
 
 constructor(private agendaservice: AgendaService,private router:Router) {}

  ngOnInit(): void {
  }

  consultar():void{
    this.agendaservice.consultarAgenda(this.agenda).subscribe(
      response=>{
        this.agenda = response.datos[0];
      }
    );

      
    }
  }

