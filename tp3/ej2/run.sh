#!/bin/bash

npm --prefix static/js run build && env python -m http.server --cgi
