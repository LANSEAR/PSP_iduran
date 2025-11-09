# 🧠 Automatizador de Tareas — Kotlin & Jetpack Compose Desktop


## 🚀✨ **EJECUCIÓN DEL PROYECTO (¡LO MÁS IMPORTANTE!)**

> 🔥 **Empieza por aquí**: esta es la forma más rápida y segura de ejecutar el programa.
Para ejecutar **Automatizador de Tareas**, asegúrate de seguir los pasos según el entorno:

### ▶️ Desde IntelliJ IDEA
1. Abre el proyecto en **IntelliJ IDEA** (recomendado: versión 2023.2 o superior).
2. Espera a que Gradle sincronice el proyecto (si aparece la opción *“Load Gradle Changes”*, haz clic en ella).
3. En el árbol del proyecto, navega hasta: src/desktopMain/kotlin/org/example/ui/MainScreen.kt
4. Abre el archivo `MainScreen.kt` y localiza la función principal:
   fun main() = application {
   Window(...)
   }
5. Haz clic en el botón ▶️ Run que aparece a la izquierda del nombre de la función.
6. La aplicación Automatizador de Tareas se iniciará y mostrará su ventana principal.

### 💾 **Usando los instaladores**

En el repositorio encontrarás la carpeta **`Instaladores/`**, que contiene los ejecutables listos para cada sistema operativo:

| Carpeta | Sistema operativo | Tipo de instalador                                              |
|----------|-------------------|-----------------------------------------------------------------|
| `Windows/` | 🪟 Windows | `.msi` (asistente de instalación)                               |
| `Linux/` | 🐧 Linux / 🍎 macOS | `ejecutable LINUX` (instalable desde terminal) |

> 💡 **Nota:** para instalar el paquete  en Linux o macOS, abre una terminal en la carpeta "TaskAutomationApp/bin" después de descomprimir el ".zip" de /Instaladores/LInux y ejecuta:
> ```bash
> sudo ./TaskAutomationApp
> ```
> Este comando permitirá ejecutar el proyecto en su equipo 


### 📘 Descripción general
**Automatizador de Tareas** es una aplicación de escritorio desarrollada en **Kotlin** con **Jetpack Compose for Desktop**, que permite crear, ejecutar y programar tareas automatizadas del sistema (limpieza de temporales, copias de seguridad, generación de informes, etc.).  
El usuario puede definir horarios de inicio y fin, configurar intervalos periódicos (cada hora, diariamente, etc.) y consultar los resultados mediante un panel de **logs**.

---

## ⚙️ Tecnologías y arquitectura

**Lenguaje:** Kotlin Multiplatform (KMP)  
**Framework UI:** Jetpack Compose for Desktop (Material Design 3)  
**Ejecución de procesos:** ProcessBuilder (JVM)  
**IDE:** IntelliJ IDEA

**Arquitectura modular:**
```
ui/               → Interfaz de usuario (MainScreen, diálogos)
logic.model/      → Modelos de datos (Task, TaskActionType, TaskViewModel)
logic.execution/  → Lógica de ejecución y planificador (CommandFactory, ProcessRunner, TaskScheduler)
```
El flujo principal es:
```
Usuario → UI (MainScreen)
        → TaskViewModel → CommandFactory → ProcessRunner
        → Logs → Interfaz
```

---

## 🧩 Funcionalidades principales

- **Crear tarea:** Define nombre, tipo y horario de funcionamiento.
- **Editar tarea:** Permite ajustar parámetros o modificar su función.
- **Programar ejecución:** Define intervalos de ejecución (15 min, 30 min, 1h, 3h, 24h).
- **Ejecutar manualmente:** Lanza una tarea en cualquier momento.
- **Detener programación:** Cancela ejecuciones periódicas.
- **Eliminar tarea:** Borra una tarea existente.
- **Panel de logs:** Muestra resultados y errores detallados.
- **Panel “Info” y “Guía”:** Información del autor, versión y ayuda de uso.

---

## 👨‍💻 Manual de usuario

1. Pulsa **“+”** para abrir el menú principal (crear, borrar o editar tarea).
2. Selecciona la acción deseada y completa los campos requeridos.
3. Desde la lista de tareas, haz clic para **ejecutar o programar** una tarea.
4. Usa el menú **⋮ (tres puntos)** para acceder a:
    - **Guía:** instrucciones básicas de uso.
    - **Info:** autor, versión y fecha de lanzamiento.
    - **Salir:** cierra la aplicación.
5. Los resultados se muestran en el panel **LOGS** (éxitos, errores o avisos).

---

## 💾 Ejemplo de tareas

| Tipo de tarea        | Descripción |
|----------------------|-------------|
| **Limpiar temporales** | Elimina archivos del directorio temporal del sistema (`/tmp` o `%TEMP%`). |
| **Copia de seguridad** | Copia de forma recursiva una carpeta de origen a destino. |
| **Generar informe**    | Crea un `.txt` en `reports/` con datos del sistema y fecha/hora. |

---

## 🧪 Pruebas realizadas

- **Copia de seguridad:** comprobada la correcta replicación de carpetas y ficheros.
- **Logs:** validado que registra correctamente la salida estándar y errores.
- **Diseño responsive:** la interfaz se adapta a cambios de tamaño de ventana.
- **Programación horaria:** ejecuta tareas solo dentro del rango horario permitido.

---

## 🧭 Conclusiones y dificultades

- Se reforzaron conocimientos de **corrutinas, hilos y ProcessBuilder**.
- El reto principal fue la **ejecución segura de comandos multiplataforma**.
- Se logró mantener una **interfaz coherente y funcional** en Compose Desktop.
- El sistema cumple los requisitos funcionales y visuales definidos.

---

## 🤖 Herramientas IA

Durante el desarrollo se empleó **ChatGPT (modelo GPT-5 de OpenAI)** como herramienta complementaria para:
- Resolver errores de compilación y proponer refactorizaciones.
- Generar código estructurado para `ProcessRunner`, `CommandFactory`, `TaskViewModel`.
- Mejorar la documentación y coherencia del texto.  
  *(El trabajo principal fue íntegramente realizado por el autor.)*

---

## 🔗 Enlace al repositorio
👉 [GitHub – Automatizador de Tareas](https://github.com/LANSEAR/PSP_iduran.git)  
