# Floki 

[![CircleCI](https://circleci.com/gh/denisidoro/floki.svg?style=svg)](https://circleci.com/gh/denisidoro/floki) 
[![npm version](https://badge.fury.io/js/floki.svg)](https://badge.fury.io/js/floki)

A JSON/EDN browser for the terminal

<img src="https://user-images.githubusercontent.com/3226564/50562060-d2868f00-0cf7-11e9-928d-f7e4d0a08cb1.gif"
     alt="Demo"
     width=520 />

### Installation

```sh
npm install -g floki
```

### Usage

```sh
echo '{"a": 42}' | floki
```

- directions: `h`, `j`, `k`, `l` or the arrow keys
- first/last item: `g`/`G`, respectively
- copy the path: `y`
- quit: `q`

### Roadmap

Please refer to the [issues page](https://github.com/denisidoro/floki/issues).

### Inspiration and template for building your own terminal UI

I've used [polymeris/hello-react-blessed](https://gist.github.com/polymeris/5e117676b79a505fe777df17f181ca2e) as a starting point.

If you want to build a tool similar to this one, use [eccentric-j/cljs-tui-template](https://github.com/eccentric-j/cljs-tui-template).

### Etymology

browser > navigator > viking > [Flóki](https://en.wikipedia.org/wiki/Hrafna-Fl%C3%B3ki_Vilger%C3%B0arson)
