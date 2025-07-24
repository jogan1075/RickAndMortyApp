# RickAndMortyApp

RickAndMortyApp es una aplicación Android desarrollada con Jetpack Compose que consume la API pública de Rick and Morty para mostrar personajes, detalles y otros datos relacionados de la serie animada.

---

## Características principales

- Arquitectura modular basada en MVVM con Clean Architecture.
- Uso de Jetpack Compose para la interfaz declarativa.
- Navegación con Navigation Compose.
- Inyección de dependencias con Hilt.
- Consumo de API REST de Rick and Morty.
- Soporte para paginación y manejo eficiente de datos.
- Módulos independientes para features como Home y Detail.
- Uso de Kotlin Coroutines para manejo asíncrono.
- Tests unitarios con MockK y Mockito.
- Temas personalizados y Material3.

---

## Estructura del proyecto

- **app**: Módulo principal que contiene Splash, navegación general y configuración global.
- **features/home**: Módulo feature que muestra la lista de personajes.
- **features/details**: Módulo feature para la pantalla de detalle del personaje.

---

## Tecnologías y librerías

- Kotlin 2.0.x
- Jetpack Compose
- Hilt para DI
- Retrofit + OkHttp para consumo de API
- Coil para carga de imágenes
- Navigation Compose
- Coroutines
- Material3
- MockK y Mockito para testing

---

## Cómo ejecutar

1. Clona el repositorio:

```bash
git clone https://github.com/jogan1075/RickAndMortyApp.git
cd RickAndMortyApp
