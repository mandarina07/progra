package com.example.proyecto

/**
 * Validar rut
 *
 * @param rut
 * @return
 */// Valida si un RUT chileno es correcto
fun validarRut(rut: String): Boolean {
    val clean = rut.replace("-", "").replace(".", "").uppercase()
    if (clean.length < 8) return false
    val cuerpo = clean.dropLast(1)
    val dv = clean.last()

    var suma = 0
    var multiplicador = 2
    for (i in cuerpo.reversed()) {
        suma += (i.digitToInt()) * multiplicador
        multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
    }
    val resto = 11 - (suma % 11)
    val dvEsperado = when (resto) {
        11 -> '0'
        10 -> 'K'
        else -> resto.digitToChar()
    }

    return dv == dvEsperado
}

/**
 * Validar email
 *
 * @param email
 * @return
 */// Valida que el correo pertenezca a Gmail
fun validarEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9._%+-]+@(gmail\\.com)$")
    return regex.matches(email)
}

/**
 * Validar fecha
 *
 * @param anio
 * @param mes
 * @return
 */// Valida que el año y mes sean validos y no estén en el futuro
fun validarFecha(anio: Int?, mes: Int?): Boolean {
    if (anio == null || mes == null) return false
    if (anio < 2000 || mes !in 1..12) return false

    val hoy = java.time.LocalDate.now()
    val fechaIngresada = java.time.YearMonth.of(anio, mes)
    val fechaActual = java.time.YearMonth.from(hoy)
    return !fechaIngresada.isAfter(fechaActual)
}
