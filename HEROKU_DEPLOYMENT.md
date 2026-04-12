# Heroku Deployment Guide

## Prerequisites
- Heroku CLI installed
- Git configured
- MongoDB Atlas URI (for `DATABASE_URI`)
- JWT Secret generated

## Setup Steps

### 1. Create Heroku App
```bash
heroku create splitso-service-<your-suffix>
```

### 2. Set Environment Variables
```bash
heroku config:set JWT_SECRET=your-secret-key-here
heroku config:set DATABASE_URI=mongodb+srv://username:password@cluster.mongodb.net/database?retryWrites=true&w=majority
```

### 3. Deploy
```bash
git push heroku main
```

### 4. View Logs
```bash
# Real-time logs
heroku logs --tail

# Last 100 lines
heroku logs --num=100
```

### 5. Test Health Endpoint
```bash
curl https://<app-name>.herokuapp.com/health
```

## Troubleshooting

### H14 Error - "No web processes running"
This means the app crashed on startup. Check logs:
```bash
heroku logs --num=200
```

Common causes:
- Missing environment variables (JWT_SECRET, DATABASE_URI)
- MongoDB connection issue
- Port configuration problem
- Circular bean dependencies

### Database Connection Issues
Ensure:
1. DATABASE_URI is correctly set
2. IP whitelist includes Heroku dynos (0.0.0.0/0)
3. MongoDB Atlas is accessible from Heroku

### No Logs Appearing
- Verify logback-spring.xml is in src/main/resources
- Check application.yaml has logging configuration
- Restart dyno: `heroku restart`

## API Endpoints

### Health Check
```
GET /health
```

### Register User
```
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password123"
}
```

### Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Performance Notes

- App uses stateless JWT authentication
- MongoDB Atlas connection pooling recommended
- Heroku free tier: single dyno, limited resources
- Consider upgrading to Hobby or Standard for production

## Port Configuration

Heroku assigns a dynamic PORT. The Procfile and application.yaml automatically handle this:
- Procfile: `-Dserver.port=${PORT}`
- application.yaml: `server.port: ${PORT:8080}`

This ensures the app listens on the correct port on Heroku while defaulting to 8080 locally.

