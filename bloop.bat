@echo off

:: %~dp0 = the folder where THIS .bat file lives
set BLOOP_HOME=%~dp0

:: Remove trailing backslash from BLOOP_HOME
if "%BLOOP_HOME:~-1%"=="\" set BLOOP_HOME=%BLOOP_HOME:~0,-1%

set OUT_DIR=%BLOOP_HOME%\out

:: Show help if no argument given
if "%~1"=="" (
    echo.
    echo   BLOOP Interpreter v1.0
    echo   ----------------------
    echo   Usage:
    echo     bloop ^<file.bloop^>
    echo     bloop ^<file.bloop^> --debug
    echo.
    echo   Examples:
    echo     bloop hello.bloop
    echo     bloop samples\program1_arithmetic.bloop
    echo     bloop myprogram.bloop --debug
    echo.
    exit /b 0
)

:: Auto-compile if needed
if not exist "%OUT_DIR%\Main.class" (
    echo   [BLOOP] Compiling interpreter for first time...
    if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

    for /r "%BLOOP_HOME%\src" %%f in (*.java) do (
        javac -d "%OUT_DIR%" "%%f"
        if errorlevel 1 (
            echo.
            echo   [BLOOP] ERROR: Interpreter failed to compile.
            echo   Make sure Java JDK 11+ is installed.
            echo.
            exit /b 1
        )
    )

    echo   [BLOOP] Interpreter ready.
    echo.
)

:: Check the .bloop file exists
if not exist "%~1" (
    echo.
    echo   [BLOOP] ERROR: File not found -- "%~1"
    echo   Check the file name and path.
    echo.
    exit /b 1
)

:: Run
java -cp "%OUT_DIR%" Main %*