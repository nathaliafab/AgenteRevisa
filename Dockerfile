FROM python:3.10-slim

ARG USER_ID=1000
ARG GROUP_ID=1000

ENV PYTHONUNBUFFERED=1

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        default-jdk-headless \
        wget \
        unzip \
        tar \
        bash \
        ca-certificates \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd -g ${GROUP_ID} appuser \
    && useradd -m -u ${USER_ID} -g ${GROUP_ID} -s /bin/bash appuser

WORKDIR /workspace

COPY requirements.txt /tmp/requirements.txt
RUN pip install --no-cache-dir -r /tmp/requirements.txt

COPY docker/entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

USER appuser

RUN find . -type f -name "*.sh" -exec sed -i 's/\r$//' {} +
RUN find . -type f -name "*.sh" -exec chmod +x {} +

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
