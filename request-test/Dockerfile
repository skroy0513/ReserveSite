FROM python:3.9-slim

WORKDIR /app

EXPOSE 80

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD [ "python", "./http_request_tool.py" ]