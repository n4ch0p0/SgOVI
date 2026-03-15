create table public.activitatformacio (
                                          tableoid oid not null,
                                          cmax cid not null,
                                          xmax xid not null,
                                          cmin cid not null,
                                          xmin xid not null,
                                          ctid tid not null,
                                          id_actividad integer primary key not null default nextval('activitatformacio_id_actividad_seq'::regclass),
                                          id_formador integer,
                                          titol character varying(250) not null,
                                          fecha timestamp without time zone,
                                          tipus tipus_af,
                                          foreign key (id_formador) references public.formador (id_formador)
                                              match simple on update cascade on delete set null
);

create table public.aprequest (
                                  tableoid oid not null,
                                  cmax cid not null,
                                  xmax xid not null,
                                  cmin cid not null,
                                  xmin xid not null,
                                  ctid tid not null,
                                  id_request integer primary key not null default nextval('aprequest_id_request_seq'::regclass),
                                  id_usuario integer not null,
                                  tipusservei tipus_ap not null,
                                  estat estat_apr default 'Revisio'::estat_apr,
                                  preferencies text,
                                  foreign key (id_usuario) references public.usuarioovi (id_usuario)
                                      match simple on update cascade on delete cascade
);

create table public.assistenciaformacio (
                                            tableoid oid not null,
                                            cmax cid not null,
                                            xmax xid not null,
                                            cmin cid not null,
                                            xmin xid not null,
                                            ctid tid not null,
                                            id_assistencia integer primary key not null default nextval('assistenciaformacio_id_assistencia_seq'::regclass),
                                            id_actividad integer not null,
                                            id_usuario integer,
                                            id_ap integer,
                                            assistencia boolean default false,
                                            url_certificat character varying(255),
                                            foreign key (id_actividad) references public.activitatformacio (id_actividad)
                                                match simple on update cascade on delete cascade,
                                            foreign key (id_ap) references public.assistentpersonal (id_ap)
                                                match simple on update cascade on delete cascade,
                                            foreign key (id_usuario) references public.usuarioovi (id_usuario)
                                                match simple on update cascade on delete cascade
);

create table public.assistentpersonal (
                                          tableoid oid not null,
                                          cmax cid not null,
                                          xmax xid not null,
                                          cmin cid not null,
                                          xmin xid not null,
                                          ctid tid not null,
                                          id_ap integer primary key not null default nextval('assistentpersonal_id_ap_seq'::regclass),
                                          estadoaceptado boolean default false,
                                          tipus tipus_ap,
                                          formacioacademica text,
                                          experienciaprevia text,
                                          disponibilitat text,
                                          proximitatgeografica text,
                                          actiu boolean default true,
                                          estat_acceptat boolean,
                                          dni character varying(10)
);

create table public.comunicaciousuariovipap (
                                                tableoid oid not null,
                                                cmax cid not null,
                                                xmax xid not null,
                                                cmin cid not null,
                                                xmin xid not null,
                                                ctid tid not null,
                                                id_comunicacio integer primary key not null default nextval('comunicaciousuariovipap_id_comunicacio_seq'::regclass),
                                                id_seleccion integer not null,
                                                mensaje text not null,
                                                fecha timestamp without time zone default CURRENT_TIMESTAMP,
                                                foreign key (id_seleccion) references public.seleccion (id_seleccion)
                                                    match simple on update cascade on delete cascade
);

create table public.formador (
                                 tableoid oid not null,
                                 cmax cid not null,
                                 xmax xid not null,
                                 cmin cid not null,
                                 xmin xid not null,
                                 ctid tid not null,
                                 id_formador integer primary key not null default nextval('formador_id_formador_seq'::regclass),
                                 nombre character varying(100) not null,
                                 especialidad character varying(100)
);

create table public.registrecontracte (
                                          tableoid oid not null,
                                          cmax cid not null,
                                          xmax xid not null,
                                          cmin cid not null,
                                          xmin xid not null,
                                          ctid tid not null,
                                          id_contracte integer primary key not null default nextval('registrecontracte_id_contracte_seq'::regclass),
                                          id_seleccion integer not null,
                                          fecha_inici date not null,
                                          fecha_fin date,
                                          pdf_path character varying(255),
                                          foreign key (id_seleccion) references public.seleccion (id_seleccion)
                                              match simple on update cascade on delete restrict
);

create table public.seleccion (
                                  tableoid oid not null,
                                  cmax cid not null,
                                  xmax xid not null,
                                  cmin cid not null,
                                  xmin xid not null,
                                  ctid tid not null,
                                  id_seleccion integer primary key not null default nextval('seleccion_id_seleccion_seq'::regclass),
                                  id_request integer not null,
                                  id_ap integer not null,
                                  fecha_propuesta date default CURRENT_DATE,
                                  foreign key (id_ap) references public.assistentpersonal (id_ap)
                                      match simple on update cascade on delete restrict,
                                  foreign key (id_request) references public.aprequest (id_request)
                                      match simple on update cascade on delete cascade
);

create table public.usuarioovi (
                                   tableoid oid not null,
                                   cmax cid not null,
                                   xmax xid not null,
                                   cmin cid not null,
                                   xmin xid not null,
                                   ctid tid not null,
                                   id_usuario integer primary key not null default nextval('usuarioovi_id_usuario_seq'::regclass),
                                   dni character varying(20) not null,
                                   contrasenya character varying(255) not null,
                                   nom character varying(100) not null,
                                   cognoms character varying(100) not null,
                                   telefono integer,
                                   email character varying(150),
                                   consentimentinformat boolean default false
);
create unique index usuarioovi_dni_key on usuarioovi using btree (dni);

