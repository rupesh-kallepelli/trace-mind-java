# TraceMind Authentication Setup

TraceMind supports authentication using:

- Google OAuth 2.0
- Microsoft Entra ID (Azure AD)

This guide explains the complete setup including:

- Google Client Configuration
- Microsoft Entra App Registration
- Frontend Configuration
- Guest User Invitation
- OpenShift Deployment Configuration

---

# Prerequisites

- Google Cloud Account
- Microsoft Azure Account
- Microsoft Entra ID Tenant
- TraceMind Frontend
- TraceMind Backend

---

# Google OAuth Setup

## Create OAuth Client

Navigate to:

```text
Google Cloud Console
→ APIs & Services
→ Credentials
→ Create Credentials
→ OAuth Client ID
```

Application Type:

```text
Web Application
```

---

## Configure Authorized JavaScript Origins

Development:

```text
http://localhost:5173
```

OpenShift:

```text
https://tracemind-ui-kallepelli-rupesh-dev.apps.rm1.0a51.p1.openshiftapps.com
```

---

## Configure Redirect URIs

Development:

```text
http://localhost:5173
```

OpenShift:

```text
https://tracemind-ui-kallepelli-rupesh-dev.apps.rm1.0a51.p1.openshiftapps.com
```

---

## Copy Client ID

Example:

```text
369280601302-xxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com
```

---

## Frontend Configuration

`.env`

```env
VITE_GOOGLE_CLIENT_ID=<google-client-id>
```

---

# Microsoft Entra ID Setup

## Create App Registration

Navigate to:

```text
Microsoft Entra ID
→ App Registrations
→ New Registration
```

Name:

```text
TraceMind
```

Supported Account Types:

```text
Accounts in any organizational directory
and personal Microsoft accounts
```

---

## Configure Authentication

Navigate to:

```text
App Registration
→ Authentication
```

Add Platform:

```text
Single Page Application (SPA)
```

Development Redirect URI:

```text
http://localhost:5173
```

OpenShift Redirect URI:

```text
https://tracemind-ui-kallepelli-rupesh-dev.apps.rm1.0a51.p1.openshiftapps.com
```

Enable:

```text
ID Tokens
Access Tokens
```

---

## Configure API Permissions

Navigate to:

```text
App Registration
→ API Permissions
```

Add:

```text
Microsoft Graph
```

Required Delegated Permissions:

```text
User.Read
openid
profile
offline_access
```

Grant Admin Consent.

---

## Capture IDs

Navigate to:

```text
App Registration
→ Overview
```

Copy:

```text
Application (client) ID
Directory (tenant) ID
```

---

## Frontend Configuration

`.env`

```env
VITE_AZURE_CLIENT_ID=<client-id>
VITE_AZURE_TENANT_ID=<tenant-id>
```

---

# MSAL Configuration

File:

```text
src/auth/authConfig.js
```

```javascript
export const msalConfig = {
  auth: {
    clientId:
      import.meta.env.VITE_AZURE_CLIENT_ID,

    authority:
      "https://login.microsoftonline.com/common",

    redirectUri:
      window.location.origin,

    navigateToLoginRequestUrl: false
  },

  cache: {
    cacheLocation: "localStorage",
    storeAuthStateInCookie: false
  }
};
```

---

# User Login Flow

## Google

```text
User
  →
Google Login
  →
Google JWT
  →
Store Token
  →
Dashboard
```

Stored Values:

```text
token
userEmail
userName
authProvider=google
```

---

## Microsoft

```text
User
  →
Microsoft Login
  →
MSAL Redirect
  →
ID Token
  →
Dashboard
```

Stored Values:

```text
token
userEmail
userName
authProvider=microsoft
```

---

# Inviting Users

## Invite External Users

Navigate to:

```text
Microsoft Entra ID
→ Users
→ New User
→ Invite External User
```

Do NOT use:

```text
Create New User
```

because that creates an internal tenant account.

---

## Example Invitations

```text
user@company.com
```

```text
user@gmail.com
```

```text
user@hcl.com
```

```text
user@outlook.com
```

---

## Invitation Workflow

```text
Administrator
   ↓
Invite User
   ↓
Invitation Email Sent
   ↓
User Accepts Invitation
   ↓
Guest Account Created
   ↓
User Logs Into TraceMind
```

---

# Logout

```javascript
const logout = () => {

  localStorage.clear();
  sessionStorage.clear();

  window.location.href = "/";
};
```

---

# Environment Variables

```env
VITE_GOOGLE_CLIENT_ID=<google-client-id>

VITE_AZURE_CLIENT_ID=<azure-client-id>

VITE_AZURE_TENANT_ID=<azure-tenant-id>
```

---

# Verification Checklist

## Google

- [ ] OAuth Client Created
- [ ] Authorized Origins Configured
- [ ] Redirect URIs Configured
- [ ] Login Successful

## Microsoft

- [ ] App Registration Created
- [ ] SPA Redirect URI Added
- [ ] API Permissions Configured
- [ ] Admin Consent Granted
- [ ] Login Successful

## User Management

- [ ] External Users Invited
- [ ] Invitation Accepted
- [ ] Guest User Visible in Entra ID
- [ ] Login Successful

---

# Supported Providers

✅ Google OAuth

✅ Microsoft Entra ID

✅ Guest Users

✅ Multi-Tenant Microsoft Login

✅ OpenShift Deployment

✅ Spring Security JWT Authentication

---

# User Access Control

TraceMind currently supports controlled user onboarding for both Google OAuth and Microsoft Entra ID.

## Google OAuth User Restrictions

During development and testing, Google OAuth can be restricted to specific users.

### Configure Test Users

Navigate to:

```text
Google Cloud Console
→ APIs & Services
→ OAuth Consent Screen
→ Audience
→ Test Users
```

Add approved users:

```text
user1@gmail.com
user2@company.com
user3@hcl.com
```

Example:

```text
kallepelli.rupesh@labgtm.com
```

### Testing Mode

When OAuth Consent Screen publishing status is:

```text
Testing
```

Only configured Test Users can sign in.

Any other user will receive an access denied message from Google.

### Production Mode

When Publishing Status is changed to:

```text
In Production
```

All Google users can authenticate, subject to backend authorization policies.

---

# Microsoft User Onboarding

## Invite External Users

Navigate to:

```text
Microsoft Entra ID
→ Users
→ New User
→ Invite External User
```

Important:

✅ Use:

```text
Invite External User
```

❌ Do NOT use:

```text
Create New User
```

because it creates a new account inside your tenant.

---

## Example Guest Users

```text
user@company.com
```

```text
user@gmail.com
```

```text
user@hcl.com
```

```text
user@outlook.com
```

```text
kallepelli.rupesh@labgtm.com
```

---

## Guest User Workflow

```text
Administrator
      ↓
Invite External User
      ↓
Invitation Email Sent
      ↓
User Accepts Invitation
      ↓
Guest Account Created
      ↓
User Appears in Entra ID Users
      ↓
User Can Login To TraceMind
```

---

## Verify User Access

Navigate to:

```text
Microsoft Entra ID
→ Users
```

Expected:

```text
User Type = Guest
```

Example:

```text
Name                  Type
--------------------------------
John Doe              Guest
Jane Smith            Guest
```

---

# Current TraceMind Authentication Strategy

## Development

Google OAuth:

```text
Status = Testing
```

Access:

```text
Only Google Test Users
```

Microsoft:

```text
Guest Users
```

Access:

```text
Only Invited Users
```

---

## Production

Google OAuth:

```text
Status = In Production
```

Microsoft:

```text
Multi-Tenant
```

Authentication:

```text
Google OAuth
Microsoft Entra ID
```

Authorization:

```text
TraceMind RBAC
Organization Rules
User Roles
```

---

# Supported Login Providers

✅ Google OAuth

✅ Microsoft Entra ID

✅ Guest Users

✅ Multi-Tenant Microsoft Authentication

✅ OpenShift Deployment

✅ Spring Security JWT Validation

✅ Enterprise User Onboarding