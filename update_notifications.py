import os
import glob
import re

directory = r'C:\Users\issei\Documents\fazfreela_front\fazfreela_frontend\src\main\resources\templates'
files = glob.glob(os.path.join(directory, '*.html'))

css_injection = """
        .nav-icon-container {
            position: relative;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }
        
        .badge {
            position: absolute;
            top: -5px;
            right: -10px;
            background: #ef4444;
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 0.7rem;
            font-weight: bold;
        }
"""

html_injection = """            <a href="/notificacoes" class="nav-icon-container" style="color: #f8fafc; text-decoration: none; font-size: 1.2rem;">
                🔔 <span class="badge" id="nav-notificacoes-badge" style="display: none;">0</span>
            </a>
"""

js_injection = """    <script>
        document.addEventListener("DOMContentLoaded", function() {
            fetch('/notificacoes/count')
                .then(response => response.json())
                .then(count => {
                    if (count > 0) {
                        const badge = document.getElementById('nav-notificacoes-badge');
                        badge.innerText = count;
                        badge.style.display = 'block';
                    }
                })
                .catch(error => console.error("Erro ao carregar notificações", error));
        });
    </script>
"""

skip_files = ['login.html', 'cadastro.html', 'notificacoes.html']

for filepath in files:
    filename = os.path.basename(filepath)
    if filename in skip_files:
        continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    if 'nav-notificacoes-badge' in content:
        print(f"Skipping {filename} as it already has notifications.")
        continue
        
    # Inject CSS
    content = content.replace('</style>', f'{css_injection}</style>')
    
    # Inject HTML
    content = re.sub(r'(<div[^>]*class="nav-links"[^>]*>\s*)', r'\g<1>' + html_injection, content, count=1)
    
    # Inject JS
    content = content.replace('</body>', f'{js_injection}</body>')
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"Updated {filename}")
