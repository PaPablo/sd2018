#!/usr/bin/env python3

from http.server import HTTPServer, BaseHTTPRequestHandler

PORT=8080
REDIRECT_PORT=8081

class CustomHTTPRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        """GET Handler"""
        if self.path == "/":
            print("REDIRECTING")
            self.send_response(301)
            new_path = "http://{}:{}/{}".format(
                "localhost", REDIRECT_PORT, "cgi-bin/index.py")
            self.send_header("Location", new_path)
            self.end_headers()

def main():
    print("STARTING SERVER ON PORT {}".format(PORT))
    # Server settings
    server_address = ("127.0.0.1", PORT)
    httpd = HTTPServer(server_address, CustomHTTPRequestHandler)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt as e:
        pass
    print("CLOSING...")
    httpd.server_close()


if __name__ == "__main__":
    main()
