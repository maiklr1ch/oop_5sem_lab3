# Лабораторна робота №3 (Android)

Створено Android-додаток (гра "Лабіринт"). Мета гри - вивести гравця з лабіринту до виходу.

## Реалізовано
- Гра “Лабіринт” на Android (Java, Views + XML)
- Відображення лабіринту через Custom View (Canvas)
- Керування гравцем свайпами (up / right / down / left)
- Рівні складності (розмір лабіринту):
    - EASY: 10x10
    - NORMAL: 18x18
    - HARD: 26x26
- Генерація лабіринту алгоритмом DFS (backtracking), прохід гарантований
- Таймер проходження
- Рекорди (Best time) збережені через SharedPreferences окремо для кожної складності

## Запуск
1. Встановити Android Studio
2. Створити проєкт (Empty Views Activity, Java)
3. Додати файли з репозиторію у відповідні каталоги `app/src/main/...`
4. Запустити на емуляторі (Device Manager > Create Virtual Device) або на реальному телефоні
## Управління
- Свайп вгору/вправо/вниз/вліво - рух гравця
- Мета: дійти до клітинки виходу (зелений квадрат)
## Приклад роботи (емулятор)
![Screen recording](https://i.ibb.co/nNYkrfgH/Screen-recording-20251214-231300.gif)

![Screenshot 1](https://i.ibb.co/6RNGJszK/Screenshot-20251214-231217.png)

![Screenshot 2](https://i.ibb.co/vxJY9c6N/Screenshot-20251214-231355.png)

![Screenshot 3](https://i.ibb.co/3m2Rb9TK/Screenshot-20251214-231401.png)

![Screenshot 4](https://i.ibb.co/8FMjGjQ/Screenshot-20251214-231410.png)

![Screenshot 5](https://i.ibb.co/SX5py0Pv/Screenshot-20251214-231419.png)
