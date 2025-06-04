package com.lar.system.file;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gstorage")
public class GoogleStorage {
    // 获取授权码
    @GetMapping("/redirectUri")
    public String redirectUri(String code) {
        System.out.println(code);
        return code;
    }

    @GetMapping("/direct")
    public String directHtml() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Authorization Successful</title>
                    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        }
                
                        body {
                            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
                            min-height: 100vh;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 20px;
                            color: #333;
                        }
                
                        .success-container {
                            background: rgba(255, 255, 255, 0.93);
                            border-radius: 20px;
                            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
                            padding: 40px 50px;
                            max-width: 500px;
                            width: 100%;
                            text-align: center;
                            position: relative;
                            overflow: hidden;
                            backdrop-filter: blur(10px);
                            border: 1px solid rgba(255, 255, 255, 0.3);
                        }
                
                        .success-container::before {
                            content: '';
                            position: absolute;
                            width: 250px;
                            height: 250px;
                            border-radius: 50%;
                            background: linear-gradient(45deg, #ff9a9e, #fad0c4);
                            top: -80px;
                            right: -80px;
                            opacity: 0.15;
                        }
                
                        .success-container::after {
                            content: '';
                            position: absolute;
                            width: 200px;
                            height: 200px;
                            border-radius: 50%;
                            background: linear-gradient(45deg, #a1c4fd, #c2e9fb);
                            bottom: -80px;
                            left: -80px;
                            opacity: 0.15;
                        }
                
                        .check-circle {
                            font-size: 80px;
                            color: #4CAF50;
                            margin-bottom: 25px;
                            position: relative;
                            z-index: 2;
                            animation: pulse 1.5s infinite;
                        }
                
                        @keyframes pulse {
                            0% { transform: scale(1); }
                            50% { transform: scale(1.1); }
                            100% { transform: scale(1); }
                        }
                
                        h1 {
                            font-size: 32px;
                            color: #2c3e50;
                            margin-bottom: 15px;
                            font-weight: 700;
                            position: relative;
                            z-index: 2;
                        }
                
                        .success-message {
                            font-size: 18px;
                            color: #555;
                            line-height: 1.6;
                            margin-bottom: 30px;
                            position: relative;
                            z-index: 2;
                        }
                
                        .timer-box {
                            background: linear-gradient(135deg, #4CAF50, #2E7D32);
                            color: white;
                            border-radius: 10px;
                            padding: 15px;
                            margin: 30px auto;
                            max-width: 280px;
                            position: relative;
                            z-index: 2;
                            box-shadow: 0 5px 15px rgba(46, 125, 50, 0.3);
                        }
                
                        .countdown {
                            font-size: 22px;
                            font-weight: 600;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            gap: 10px;
                        }
                
                        .countdown-number {
                            background: white;
                            color: #4CAF50;
                            width: 50px;
                            height: 50px;
                            border-radius: 50%;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            font-size: 24px;
                            font-weight: 700;
                            box-shadow: 0 5px 10px rgba(0,0,0,0.1);
                        }
                
                        .close-btn {
                            background: #2c3e50;
                            color: white;
                            border: none;
                            padding: 12px 30px;
                            font-size: 16px;
                            border-radius: 30px;
                            cursor: pointer;
                            transition: all 0.3s ease;
                            font-weight: 600;
                            position: relative;
                            z-index: 2;
                            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                        }
                
                        .close-btn:hover {
                            background: #1a2530;
                            transform: translateY(-2px);
                            box-shadow: 0 7px 15px rgba(0,0,0,0.2);
                        }
                
                        .footer-note {
                            margin-top: 25px;
                            font-size: 14px;
                            color: #777;
                            position: relative;
                            z-index: 2;
                        }
                
                        @media (max-width: 600px) {
                            .success-container {
                                padding: 30px;
                            }
                
                            .check-circle {
                                font-size: 60px;
                            }
                
                            h1 {
                                font-size: 26px;
                            }
                
                            .success-message {
                                font-size: 16px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="success-container">
                        <div class="check-circle">
                            <i class="fas fa-check-circle"></i>
                        </div>
                
                        <h1>Authorization Successful!</h1>
                
                        <div class="success-message">
                            <p>Your account has been successfully authorized.</p>
                            <p>You now have full access to all premium features.</p>
                        </div>
                
                        <div class="timer-box">
                            <p>This page will close automatically in</p>
                            <div class="countdown">
                                <div class="countdown-number" id="seconds">5</div>
                                <span>seconds</span>
                            </div>
                        </div>
                
                        <button class="close-btn" onclick="window.close()">
                            <i class="fas fa-times-circle"></i> Close Window Now
                        </button>
                
                        <p class="footer-note">If the page doesn't close automatically, please click the button above.</p>
                    </div>
                
                    <script>
                        let seconds = 5;
                        const timerElement = document.getElementById('seconds');
                
                        const countdown = setInterval(() => {
                            seconds--;
                            timerElement.textContent = seconds;
                
                            if (seconds === 0) {
                                clearInterval(countdown);
                                try {
                                    window.close();
                                } catch (e) {
                                    console.log("Window could not be closed automatically");
                                }
                            }
                        }, 1000);
                    </script>
                </body>
                </html>
                """;
    }

}
