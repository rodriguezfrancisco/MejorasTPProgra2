import Interfaces.*;
import TDA.Arbol_Categoria;
import TDA.DiccionarioUsuarios;
import TDA.GrafoListaAdyacencia;
import TDA.IndiceInvertidoHabilidades;
import controller.GestorPerfil;
import controller.GestorPostulaciones;
import model.*;
import view.BuscadorPerfilesUI;
import view.SelectorPerfil;

import java.util.Scanner;

public class ProgramaPrincipal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Arbol_Categoria catalogoGeneral = inicializarCatalogo();
        DiccionarioUsuarios plataforma = new DiccionarioUsuarios(100);

        TDA.IndiceInvertidoHabilidades indiceHabilidades = new TDA.IndiceInvertidoHabilidades(200);

        GestorPerfil gestorPerfil = new GestorPerfil(10, catalogoGeneral);
        GrafoListaAdyacencia redSocial = new GrafoListaAdyacencia(100, false);
        GestorPostulaciones gestorPostulaciones = new GestorPostulaciones(50);

        precargarUsuarios(plataforma, redSocial, catalogoGeneral, indiceHabilidades);
        int opcion;

        System.out.println("=============================================");
        System.out.println("   ECOSISTEMA DE RED SOCIAL PROFESIONAL      ");
        System.out.println("=============================================");

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Crear nuevo usuario");
            System.out.println("2. Buscar usuario por ID");
            System.out.println("3. Editar usuario existente");
            System.out.println("4. Deshacer ultimo cambio");
            System.out.println("5. Eliminar usuario de la plataforma");
            System.out.println("6. Conectar dos usuarios (Agregar Amistad)");
            System.out.println("7. Ver recomendaciones y red de un usuario");
            System.out.println("8. Gestion de postulaciones");
            System.out.println("9. Buscar usuarios por perfil");
            System.out.println("0. Salir");

            opcion = SelectorPerfil.leerEnteroEnRango(scanner, "\nSeleccione una opcion: ", 0, 9);

            switch (opcion) {
                case 1:
                    Usuario nuevoUsuario = gestorPerfil.completarUsuario(scanner, plataforma);
                    boolean exito = plataforma.insertar(nuevoUsuario);
                    if (exito) {
                        redSocial.insertarVertice(nuevoUsuario);

                        indiceHabilidades.indexarUsuario(nuevoUsuario);

                        System.out.println("-> Usuario guardado exitosamente...");
                    }
                    break;

                case 2:
                    System.out.println("\n--- BUSCAR USUARIO ---");
                    int idBuscar = SelectorPerfil.leerEntero(scanner, "Ingrese el dni a buscar: ");
                    Usuario encontrado = plataforma.buscar(idBuscar);

                    if (encontrado != null) {
                        System.out.println("\nUSUARIO ENCONTRADO:");
                        System.out.println(encontrado);
                    } else {
                        System.out.println("\n-> Error: No existe ningun usuario con el ID " + idBuscar);
                    }
                    break;

                case 3:
                    System.out.println("\n--- EDITAR USUARIO ---");
                    int idEditar = SelectorPerfil.leerEntero(scanner, "Ingrese el ID del usuario a editar: ");
                    Usuario usuarioAEditar = plataforma.buscar(idEditar);

                    if (usuarioAEditar != null) {
                        gestorPerfil.editarUsuario(scanner, usuarioAEditar);
                    } else {
                        System.out.println("\n-> Error: No existe ningun usuario con el ID " + idEditar);
                    }

                    break;

                case 4:
                    System.out.println("\n--- DESHACER CAMBIOS ---");
                    int idDeshacer = SelectorPerfil.leerEntero(scanner, "Ingrese el ID del usuario: ");
                    Usuario usuarioADeshacer = plataforma.buscar(idDeshacer);

                    if (usuarioADeshacer != null) {
                        gestorPerfil.deshacerCambio(usuarioADeshacer);
                    } else {
                        System.out.println("\n-> Error: No existe ningun usuario con el ID " + idDeshacer);
                    }

                    break;

                case 5:
                    System.out.println("\n--- ELIMINAR USUARIO ---");
                    int idEliminar = SelectorPerfil.leerEntero(scanner, "Ingrese el ID del usuario a eliminar: ");

                    Usuario usuarioAEliminar = plataforma.buscar(idEliminar);
                    if (usuarioAEliminar != null) {
                        plataforma.eliminar(idEliminar);
                        redSocial.eliminarVertice(usuarioAEliminar);
                        System.out.println("\n-> Usuario eliminado con exito de la plataforma y sus conexiones.");
                    } else {
                        System.out.println("\n-> Error: No se encontro un usuario con ese ID.");
                    }

                    break;

                case 6:
                    System.out.println("\n--- CONECTAR USUARIOS ---");
                    int id1 = SelectorPerfil.leerEntero(scanner, "Ingrese el ID del primer usuario: ");
                    int id2 = SelectorPerfil.leerEntero(scanner, "Ingrese el ID del segundo usuario: ");

                    Usuario u1 = plataforma.buscar(id1);
                    Usuario u2 = plataforma.buscar(id2);

                    if (u1 != null && u2 != null) {
                        redSocial.insertarArista(u1, u2);
                        System.out.println("-> ¡" + u1.getNombre() + " y " + u2.getNombre() + " ahora estan conectados!");
                    } else {
                        System.out.println("-> Error: Uno o ambos IDs no existen en la plataforma.");
                    }
                    break;

                case 7:
                    System.out.println("\n--- MI RED Y RECOMENDACIONES ---");
                    int idRed = SelectorPerfil.leerEntero(scanner, "Ingrese el ID del usuario: ");
                    Usuario uRed = plataforma.buscar(idRed);

                    if (uRed != null) {
                        System.out.println();
                        redSocial.bfsNiveles(uRed); // Muestra los niveles de separación
                        System.out.println();
                        redSocial.recomendarAmigos(uRed); // Sugiere los de nivel 2
                    } else {
                        System.out.println("-> Error: Usuario no encontrado.");
                    }
                    break;
                    
                case 8:
                    int subOpcion8;
                    do {
                        System.out.println("\n--- GESTIÓN DE POSTULACIONES ---");
                        System.out.println("1. Ingresar nueva postulación");
                        System.out.println("2. Ver próxima postulación en la fila");
                        System.out.println("3. Evaluar (Aceptar/Rechazar) próxima postulación");
                        System.out.println("0. Volver al menú principal");

                        subOpcion8 = SelectorPerfil.leerEnteroEnRango(scanner, "\nSeleccione una opción: ", 0, 3);

                        switch (subOpcion8) {
                            case 1:
                                System.out.println("\n-- Registrar Nueva Postulación --");
                                int idCandidato = SelectorPerfil.leerEntero(scanner, "Ingrese el ID (DNI) del usuario registrado: ");

                                // Buscamos al usuario en la tabla hash de forma instantánea
                                Usuario candidato = plataforma.buscar(idCandidato);

                                if (candidato == null) {
                                    System.out.println("❌ Error: No existe ningún usuario registrado con el ID " + idCandidato);
                                    System.out.println("Debe crearlo primero en el menú principal.");
                                } else if (candidato.getPerfil() == null) {
                                    // Opcional: validar que tenga perfil antes de postularse
                                    System.out.println("❌ Error: El usuario " + candidato.getNombre() + " todavía no tiene un perfil configurado.");
                                    System.out.println("Vaya a 'Editar usuario' (Opción 3) para agregarle un perfil.");
                                } else {
                                    System.out.println("\n✅ Candidato encontrado: " + candidato.getNombre());
                                    System.out.println("Seleccione el puesto al que se postula:");

                                    Postulacion[] puestos = Postulacion.values();
                                    for (int i = 0; i < puestos.length; i++) {
                                        System.out.println((i + 1) + ". " + puestos[i].getNombrePuesto() + " - " + puestos[i].getEmpresa());
                                    }

                                    int puestoOpcion = SelectorPerfil.leerEnteroEnRango(scanner, "Opción: ", 1, puestos.length);
                                    Postulacion puestoElegido = puestos[puestoOpcion - 1];

                                    // Pasamos el objeto Usuario completo a la solicitud
                                    gestorPostulaciones.recibirSolicitud(new SolicitudEmpleo(candidato, puestoElegido));
                                }
                                break;

                            case 2:
                                System.out.println("\n-- Próxima Postulación a Revisar --");
                                gestorPostulaciones.verProximaSolicitud();
                                break;

                            case 3:
                                System.out.println("\n-- Evaluar Postulación --");
                                gestorPostulaciones.evaluarSiguienteSolicitud(scanner);
                                break;
                        }
                    } while (subOpcion8 != 0);
                    break;

                case 9:
                    BuscadorPerfilesUI.menuBuscar(scanner, plataforma, catalogoGeneral, indiceHabilidades);
                    break;
                    
                case 0:
                    System.out.println("\nSaliendo del sistema... ¡Hasta luego!");
                    break;
            }

        } while (opcion != 0);

        scanner.close();
    }
    private static Arbol_Categoria inicializarCatalogo() {
        Arbol_Categoria raiz = new Arbol_Categoria("Catalogo de Especialidades");

        // ==========================================
        // 0. DEFINIMOS LAS HABILIDADES BLANDAS (Una sola vez)
        // ==========================================
        Habilidad[] habilidadesBlandas = {
                new Habilidad("Comunicacion", true),
                new Habilidad("Trabajo en equipo", true),
                new Habilidad("Liderazgo", true),
                new Habilidad("Responsabilidad", true),
                new Habilidad("Organizacion", true),
                new Habilidad("Resolucion de problemas", true),
                new Habilidad("Pensamiento critico", true),
                new Habilidad("Adaptabilidad", true),
                new Habilidad("Creatividad", true),
                new Habilidad("Empatia", true),
                new Habilidad("Negociacion", true),
                new Habilidad("Manejo del tiempo", true),
                new Habilidad("Proactividad", true),
                new Habilidad("Autonomia", true),
                new Habilidad("Atencion al detalle", true)
        };

        // ==========================================
        // 1. TECNOLOGIA
        // ==========================================
        Arbol_Categoria tecnologia = new Arbol_Categoria("Tecnologia");

        Arbol_Categoria tecDesarrollo = new Arbol_Categoria("Desarrollo");
        tecDesarrollo.agregar(new Habilidad("Java", false));
        tecDesarrollo.agregar(new Habilidad("Python", false));
        tecDesarrollo.agregar(new Habilidad("JavaScript", false));
        tecDesarrollo.agregar(new Habilidad("Go", false));
        tecDesarrollo.agregar(new Habilidad("Rust", false));

        Arbol_Categoria tecBaseDatos = new Arbol_Categoria("Bases de datos");
        tecBaseDatos.agregar(new Habilidad("SQL", false));
        tecBaseDatos.agregar(new Habilidad("MongoDB", false));
        tecBaseDatos.agregar(new Habilidad("DynamoDB", false));

        Arbol_Categoria tecIA = new Arbol_Categoria("Inteligencia Artificial");
        tecIA.agregar(new Habilidad("Machine Learning", false));
        tecIA.agregar(new Habilidad("Deep Learning", false));
        tecIA.agregar(new Habilidad("NLP", false));
        tecIA.agregar(new Habilidad("Vision Computacional", false));

        Arbol_Categoria tecDevOps = new Arbol_Categoria("DevOps");
        tecDevOps.agregar(new Habilidad("Docker", false));
        tecDevOps.agregar(new Habilidad("Kubernetes", false));
        tecDevOps.agregar(new Habilidad("AWS", false));
        tecDevOps.agregar(new Habilidad("CI/CD", false));

        Arbol_Categoria tecSistemasDistribuidos = new Arbol_Categoria("Sistemas Distribuidos");
        tecSistemasDistribuidos.agregar(new Habilidad("Microservicios", false));
        tecSistemasDistribuidos.agregar(new Habilidad("Message Queues", false));
        tecSistemasDistribuidos.agregar(new Habilidad("Cache distribuido", false));

        tecnologia.agregar(tecDesarrollo);
        tecnologia.agregar(tecBaseDatos);
        tecnologia.agregar(tecIA);
        tecnologia.agregar(tecDevOps);
        tecnologia.agregar(tecSistemasDistribuidos);
        raiz.agregar(tecnologia);

        // ==========================================
        // 2. DISEÑO
        // ==========================================
        Arbol_Categoria diseno = new Arbol_Categoria("Diseno");

        Arbol_Categoria disGrafico = new Arbol_Categoria("Diseño grafico");
        disGrafico.agregar(new Habilidad("Branding", false));
        disGrafico.agregar(new Habilidad("Tipografia", false));
        disGrafico.agregar(new Habilidad("Ilustracion digital", false));

        Arbol_Categoria disUX = new Arbol_Categoria("UX/UI");
        disUX.agregar(new Habilidad("Wireframing", false));
        disUX.agregar(new Habilidad("Prototyping", false));
        disUX.agregar(new Habilidad("User Research", false));

        Arbol_Categoria dis3D = new Arbol_Categoria("Diseño 3D");
        dis3D.agregar(new Habilidad("Modelado 3D", false));
        dis3D.agregar(new Habilidad("Renderizado", false));
        dis3D.agregar(new Habilidad("Animacion 3D", false));

        Arbol_Categoria disWeb = new Arbol_Categoria("Diseño Web");
        disWeb.agregar(new Habilidad("CSS", false));
        disWeb.agregar(new Habilidad("Responsive Design", false));
        disWeb.agregar(new Habilidad("Accesibilidad Web", false));

        diseno.agregar(disGrafico);
        diseno.agregar(disUX);
        diseno.agregar(dis3D);
        diseno.agregar(disWeb);
        raiz.agregar(diseno);

        // ==========================================
        // 3. NEGOCIOS
        // ==========================================
        Arbol_Categoria negocios = new Arbol_Categoria("Negocios");

        Arbol_Categoria negMarketing = new Arbol_Categoria("Marketing");
        negMarketing.agregar(new Habilidad("Marketing Digital", false));
        negMarketing.agregar(new Habilidad("Social Media", false));
        negMarketing.agregar(new Habilidad("Analisis de datos", false));

        Arbol_Categoria negVentas = new Arbol_Categoria("Ventas");
        negVentas.agregar(new Habilidad("Prospecting", false));
        negVentas.agregar(new Habilidad("Cierre de ventas", false));

        Arbol_Categoria negFinanzas = new Arbol_Categoria("Finanzas");
        negFinanzas.agregar(new Habilidad("Analisis financiero", false));
        negFinanzas.agregar(new Habilidad("Presupuestacion", false));

        Arbol_Categoria negContabilidad = new Arbol_Categoria("Contabilidad");
        negContabilidad.agregar(new Habilidad("Contabilidad General", false));
        negContabilidad.agregar(new Habilidad("Impuestos", false));

        Arbol_Categoria negProject = new Arbol_Categoria("Project Management");
        negProject.agregar(new Habilidad("Metodologia Agile", false));
        negProject.agregar(new Habilidad("SCRUM", false));

        Arbol_Categoria negRRHH = new Arbol_Categoria("Recursos Humanos");
        negRRHH.agregar(new Habilidad("Reclutamiento", false));
        negRRHH.agregar(new Habilidad("Capacitacion", false));

        Arbol_Categoria negEmprendimiento = new Arbol_Categoria("Emprendimiento");
        negEmprendimiento.agregar(new Habilidad("Business Model Canvas", false));
        negEmprendimiento.agregar(new Habilidad("Pitch", false));

        negocios.agregar(negMarketing);
        negocios.agregar(negVentas);
        negocios.agregar(negFinanzas);
        negocios.agregar(negContabilidad);
        negocios.agregar(negProject);
        negocios.agregar(negRRHH);
        negocios.agregar(negEmprendimiento);
        raiz.agregar(negocios);

        // ==========================================
        // 4. CIENCIAS JURIDICAS
        // ==========================================
        Arbol_Categoria juridicas = new Arbol_Categoria("Ciencias Juridicas");

        Arbol_Categoria jurCivil = new Arbol_Categoria("Derecho civil");
        jurCivil.agregar(new Habilidad("Contratos", false));
        jurCivil.agregar(new Habilidad("Derecho de familia", false));

        Arbol_Categoria jurPenal = new Arbol_Categoria("Derecho penal");
        jurPenal.agregar(new Habilidad("Defensa penal", false));

        Arbol_Categoria jurLaboral = new Arbol_Categoria("Derecho laboral");
        jurLaboral.agregar(new Habilidad("Liquidaciones laborales", false));

        Arbol_Categoria jurComercial = new Arbol_Categoria("Derecho comercial");
        jurComercial.agregar(new Habilidad("Sociedades comerciales", false));

        Arbol_Categoria jurInformatico = new Arbol_Categoria("Derecho informatico");
        jurInformatico.agregar(new Habilidad("Proteccion de datos", false));

        juridicas.agregar(jurCivil);
        juridicas.agregar(jurPenal);
        juridicas.agregar(jurLaboral);
        juridicas.agregar(jurComercial);
        juridicas.agregar(jurInformatico);
        raiz.agregar(juridicas);

        // ==========================================
        // 5. COMUNICACION
        // ==========================================
        Arbol_Categoria comunicacion = new Arbol_Categoria("Comunicacion");

        Arbol_Categoria comPeriodismo = new Arbol_Categoria("Periodismo");
        comPeriodismo.agregar(new Habilidad("Redaccion periodistica", false));

        Arbol_Categoria comInstitucional = new Arbol_Categoria("Comunicacion institucional");
        comInstitucional.agregar(new Habilidad("Comunicados institucionales", false));

        Arbol_Categoria comRedaccion = new Arbol_Categoria("Redaccion");
        comRedaccion.agregar(new Habilidad("Redaccion creativa", false));

        Arbol_Categoria comAudiovisual = new Arbol_Categoria("Produccion audiovisual");
        comAudiovisual.agregar(new Habilidad("Edicion de video", false));

        Arbol_Categoria comRRPr = new Arbol_Categoria("Relaciones publicas");
        comRRPr.agregar(new Habilidad("Prensa", false));

        comunicacion.agregar(comPeriodismo);
        comunicacion.agregar(comInstitucional);
        comunicacion.agregar(comRedaccion);
        comunicacion.agregar(comAudiovisual);
        comunicacion.agregar(comRRPr);
        raiz.agregar(comunicacion);

        // ==========================================
        // 6. SALUD
        // ==========================================
        Arbol_Categoria salud = new Arbol_Categoria("Salud");

        Arbol_Categoria salMedicina = new Arbol_Categoria("Medicina");
        salMedicina.agregar(new Habilidad("Atencion clinica", false));

        Arbol_Categoria salPsicologia = new Arbol_Categoria("Psicologia");
        salPsicologia.agregar(new Habilidad("Terapia individual", false));

        Arbol_Categoria salNutricion = new Arbol_Categoria("Nutricion");
        salNutricion.agregar(new Habilidad("Plan nutricional", false));

        Arbol_Categoria salEnfermeria = new Arbol_Categoria("Enfermeria");
        salEnfermeria.agregar(new Habilidad("Cuidados de enfermeria", false));

        Arbol_Categoria salKinesiologia = new Arbol_Categoria("Kinesiologia");
        salKinesiologia.agregar(new Habilidad("Rehabilitacion", false));

        salud.agregar(salMedicina);
        salud.agregar(salPsicologia);
        salud.agregar(salNutricion);
        salud.agregar(salEnfermeria);
        salud.agregar(salKinesiologia);
        raiz.agregar(salud);

        // ==========================================
        // 7. ENTRETENIMIENTO
        // ==========================================
        Arbol_Categoria entretenimiento = new Arbol_Categoria("Entretenimiento");

        Arbol_Categoria entMusica = new Arbol_Categoria("Musica");
        entMusica.agregar(new Habilidad("Canto", false));

        Arbol_Categoria entCine = new Arbol_Categoria("Cine");
        entCine.agregar(new Habilidad("Guion", false));

        Arbol_Categoria entTeatro = new Arbol_Categoria("Teatro");
        entTeatro.agregar(new Habilidad("Actuacion", false));

        Arbol_Categoria entVideojuegos = new Arbol_Categoria("Videojuegos");
        entVideojuegos.agregar(new Habilidad("Game design", false));

        Arbol_Categoria entStreaming = new Arbol_Categoria("Streaming");
        entStreaming.agregar(new Habilidad("Produccion de streaming", false));

        entretenimiento.agregar(entMusica);
        entretenimiento.agregar(entCine);
        entretenimiento.agregar(entTeatro);
        entretenimiento.agregar(entVideojuegos);
        entretenimiento.agregar(entStreaming);
        raiz.agregar(entretenimiento);

        // ==========================================
        // 8. TURISMO
        // ==========================================
        Arbol_Categoria turismo = new Arbol_Categoria("Turismo");

        Arbol_Categoria turHoteleria = new Arbol_Categoria("Hoteleria");
        turHoteleria.agregar(new Habilidad("Recepcion hotelera", false));

        Arbol_Categoria turGuia = new Arbol_Categoria("Guia turistico");
        turGuia.agregar(new Habilidad("Guia de turismo", false));

        Arbol_Categoria turAgencia = new Arbol_Categoria("Agencia de viajes");
        turAgencia.agregar(new Habilidad("Armado de paquetes", false));

        Arbol_Categoria turGastronomia = new Arbol_Categoria("Gastronomia");
        turGastronomia.agregar(new Habilidad("Cocina", false));

        Arbol_Categoria turEventos = new Arbol_Categoria("Eventos");
        turEventos.agregar(new Habilidad("Organizacion de eventos", false));

        turismo.agregar(turHoteleria);
        turismo.agregar(turGuia);
        turismo.agregar(turAgencia);
        turismo.agregar(turGastronomia);
        turismo.agregar(turEventos);
        raiz.agregar(turismo);

        // ==========================================
        // 9. OTROS
        // ==========================================
        Arbol_Categoria otros = new Arbol_Categoria("Otros");

        Arbol_Categoria otrSub = new Arbol_Categoria("Otros");
        otrSub.agregar(new Habilidad("Oficios", false));
        otrSub.agregar(new Habilidad("Capacitacion", false));

        otros.agregar(otrSub);
        raiz.agregar(otros);

        // ==========================================
        // INYECCIÓN AUTOMÁTICA DE HABILIDADES BLANDAS
        // ==========================================
        // Recorremos el árbol: Raiz -> Especialidades -> Subespecialidades
        Componente[] especialidades = raiz.getHijos();

        for (int i = 0; i < especialidades.length; i++) {
            Arbol_Categoria especialidad = (Arbol_Categoria) especialidades[i];
            Componente[] subespecialidades = especialidad.getHijos();

            for (int j = 0; j < subespecialidades.length; j++) {
                Arbol_Categoria sub = (Arbol_Categoria) subespecialidades[j];

                // Le agregamos a cada subespecialidad todas las habilidades blandas
                for (int k = 0; k < habilidadesBlandas.length; k++) {
                    sub.agregar(habilidadesBlandas[k]);
                }
            }
        }

        return raiz;
    }

    private static void precargarUsuarios(DiccionarioUsuarios plataforma, GrafoListaAdyacencia redSocial, Arbol_Categoria raiz, IndiceInvertidoHabilidades indiceHabilidades) {
        Componente[] especialidades = raiz.getHijos();

        // 1. Buscamos las Especialidades principales en el árbol
        Arbol_Categoria tecnologia = buscarCategoria(especialidades, "Tecnologia");
        Arbol_Categoria diseno = buscarCategoria(especialidades, "Diseno");
        Arbol_Categoria negocios = buscarCategoria(especialidades, "Negocios");

        // --- USUARIO 1: Desarrollador (Tecnología > Desarrollo) ---
        if (tecnologia != null) {
            Arbol_Categoria desarrollo = buscarCategoria(tecnologia.getHijos(), "Desarrollo");
            if (desarrollo != null) {
                Habilidad hJava = buscarHabilidad(desarrollo, "Java");
                Habilidad hPython = buscarHabilidad(desarrollo, "Python");
                Habilidad hCom = buscarHabilidad(desarrollo, "Comunicacion"); // Inyectada automáticamente

                Habilidad[] habsU1 = { hJava, hPython, hCom };
                Perfil perfilU1 = new Perfil(tecnologia, desarrollo, habsU1);
                Usuario u1 = new Usuario("Carlos Gómez", 1, "carlos@mail.com", perfilU1);

                plataforma.insertar(u1);
                indiceHabilidades.indexarUsuario(u1);
                redSocial.insertarVertice(u1);
            }
        }

        // --- USUARIO 2: Diseñadora (Diseño > UX/UI) ---
        if (diseno != null) {
            Arbol_Categoria uxui = buscarCategoria(diseno.getHijos(), "UX/UI");
            if (uxui != null) {
                Habilidad hWire = buscarHabilidad(uxui, "Wireframing");
                Habilidad hProto = buscarHabilidad(uxui, "Prototyping");
                Habilidad hTeam = buscarHabilidad(uxui, "Trabajo en equipo");

                Habilidad[] habsU2 = { hWire, hProto, hTeam };
                Perfil perfilU2 = new Perfil(diseno, uxui, habsU2);
                Usuario u2 = new Usuario("Marta Rodríguez", 2, "marta@mail.com", perfilU2);

                plataforma.insertar(u2);
                indiceHabilidades.indexarUsuario(u2);
                redSocial.insertarVertice(u2);
            }
        }

        // --- USUARIO 3: Project Manager (Negocios > Project Management) ---
        if (negocios != null) {
            Arbol_Categoria pm = buscarCategoria(negocios.getHijos(), "Project Management");
            if (pm != null) {
                Habilidad hAgile = buscarHabilidad(pm, "Metodologia Agile");
                Habilidad hScrum = buscarHabilidad(pm, "SCRUM");
                Habilidad hLead = buscarHabilidad(pm, "Liderazgo");

                Habilidad[] habsU3 = { hAgile, hScrum, hLead };
                Perfil perfilU3 = new Perfil(negocios, pm, habsU3);
                Usuario u3 = new Usuario("Esteban Pérez", 3, "esteban@mail.com", perfilU3);

                plataforma.insertar(u3);
                indiceHabilidades.indexarUsuario(u3);
                redSocial.insertarVertice(u3);
            }
        }

        System.out.println("[Sistema] Se han precargado 3 usuarios de prueba con perfiles configurados.");
    }

    // Busca un Arbol_Categoria por su nombre dentro de un arreglo de Componentes
    private static Arbol_Categoria buscarCategoria(Componente[] componentes, String nombre) {
        if (componentes == null) return null;
        for (int i = 0; i < componentes.length; i++) {
            if (componentes[i].getNombre().equalsIgnoreCase(nombre)) {
                return (Arbol_Categoria) componentes[i];
            }
        }
        return null;
    }

    // Busca una Habilidad por su nombre dentro de una subcategoría
    private static Habilidad buscarHabilidad(Arbol_Categoria subcategoria, String nombre) {
        if (subcategoria == null) return null;
        Componente[] habilidades = subcategoria.getHijos();
        for (int i = 0; i < habilidades.length; i++) {
            if (habilidades[i].getNombre().equalsIgnoreCase(nombre)) {
                return (Habilidad) habilidades[i];
            }
        }
        return null;
    }
}
