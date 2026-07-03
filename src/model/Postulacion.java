package model;

public enum Postulacion {

    // --- TECNOLOGIA ---
    DEV_FULLSTACK("Desarrollador Fullstack Senior", "PixelForge Solutions", "Tecnologia"),
    DATA_ENGINEER("Ingeniero de Datos Cloud", "DataStream Analytics", "Tecnologia"),

    // --- MARKETING ---
    GROWTH_MARKETER("Growth Marketing Specialist", "LeadPulse Media", "Marketing"),
    ANALISTA_SEO_SEM("Analista SEO/SEM Internacional", "RankUp Agency", "Marketing"),

    // --- DISENO ---
    UX_UI_DESIGNER("Product Designer (UX/UI)", "UserFirst Studio", "Diseno"),
    DISEÑADOR_GRAFICO("Diseñador Gráfico Creativo", "Kroma Concept", "Diseno"),

    // --- NEGOCIOS ---
    ANALISTA_NEGOCIO("Analista de Negocios BI", "VentureScale Consulting", "Negocios"),
    PROJECT_MANAGER("Project Manager Agile", "SynergyOps", "Negocios"),

    // --- CIENCIAS JURIDICAS ---
    ASESOR_LEGAL("Asesor Legal Corporativo", "LexShield Partners", "Ciencias Juridicas"),
    CUMPLIMIENTO("Oficial de Cumplimiento Tech", "TrustArmor Legal", "Ciencias Juridicas"),

    // --- COMUNICACION ---
    REDACTOR("Redactor Creativo & Copywriter", "WordCraft Agency", "Comunicacion"),
    COMMUNITY_MANAGER("Social Media & Community Lead", "BuzzViral", "Comunicacion"),

    // --- SALUD ---
    DOCTOR_VIRTUAL("Médico de Telemedicina", "TeleSalud Global", "Salud"),
    CONSULTOR_BIENESTAR("Consultor de Bienestar Corporativo", "VitalityCorp", "Salud"),

    // --- ENTRETENIMIENTO ---
    DISEÑADOR_VIDEOJUEGOS("Diseñador de Videojuegos Mobile", "NeonPlay Studios", "Entretenimiento"),
    VIDEO_EDITOR("Editor de Video & Postproductor", "CutFrame Media", "Entretenimiento"),

    // --- TURISMO ---
    COORDINADOR_VIAJE("Coordinador de Experiencias VIP", "WanderLust Travel", "Turismo"),
    HOTEL_MANAGER("Gerente de Operaciones Hoteleras", "Zenith Resorts", "Turismo"),

    // --- OTROS ---
    ASISTENTE_VIRTUAL("Asistente Virtual Bilingüe", "RemoteTask", "Otros");

    private final String nombrePuesto;
    private final String empresa;
    private final String especialidadCategoria; // Ahora es un String para acoplarse al árbol de Categorías

    // Constructor modificado
    Postulacion(String nombrePuesto, String empresa, String especialidadCategoria) {
        this.nombrePuesto = nombrePuesto;
        this.empresa = empresa;
        this.especialidadCategoria = especialidadCategoria;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getEspecialidadCategoria() {
        return especialidadCategoria;
    }

    @Override
    public String toString() {
        return nombrePuesto + " en " + empresa + " (" + especialidadCategoria + ")";
    }
}