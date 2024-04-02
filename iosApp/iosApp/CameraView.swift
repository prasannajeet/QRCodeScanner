//
//  CameraView.swift
//  iosApp
//
//  Created by Prasan Pani on 2024-03-24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import AVFoundation

struct CameraView: View {
    @State private var isCameraAuthorized = false

    var body: some View {
        ZStack {
            if isCameraAuthorized {
                CameraPreview() // We'll create this next
            } else {
                Button("Request Camera Access") {
                    requestCameraAuthorization()
                }
            }
        }
        .onAppear {
            checkCameraAuthorization()
        }
    }

    private func checkCameraAuthorization() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
            case .authorized:
                isCameraAuthorized = true
            case .notDetermined:
                AVCaptureDevice.requestAccess(for: .video) { granted in
                    isCameraAuthorized = granted
                }
            default:
                isCameraAuthorized = false
        }
    }

    private func requestCameraAuthorization() {
        AVCaptureDevice.requestAccess(for: .video) { granted in
            isCameraAuthorized = granted
        }
    }
}

struct CameraPreview: UIViewRepresentable {
    class VideoPreviewView: UIView {
        override class var layerClass: AnyClass {
            AVCaptureVideoPreviewLayer.self
        }

        var videoPreviewLayer: AVCaptureVideoPreviewLayer {
            return layer as! AVCaptureVideoPreviewLayer
        }
    }

    let session = AVCaptureSession()

    func makeUIView(context: Context) -> UIView {
        let view = VideoPreviewView()

        startCameraSession(on: view)

        return view
    }

    func updateUIView(_ uiView: UIView, context: Context) {
        // Not used in basic setup
    }

    private func startCameraSession(on view: VideoPreviewView) {
        session.beginConfiguration()
        defer { session.commitConfiguration() }

        guard let device = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back),
              let input = try? AVCaptureDeviceInput(device: device),
              session.canAddInput(input) else {
            // Handle error if no camera or input cannot be added
            return
        }
        session.addInput(input)

        let output = AVCaptureVideoDataOutput()
        if session.canAddOutput(output) {
            session.addOutput(output)
        }

        view.videoPreviewLayer.session = session
        view.videoPreviewLayer.videoGravity = .resizeAspectFill

        DispatchQueue.global().async {
            self.session.startRunning()
        }
    }
}

class CameraViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        // Create an instance of your SwiftUI view
        let swiftUIView = CameraView()

        // Initialize a UIHostingController with your SwiftUI view as the root
        let hostingController = UIHostingController(rootView: swiftUIView)

        // Add the hostingController as a child view controller.
        addChild(hostingController)

        // Add the hostingController's view to your UIViewController's view hierarchy
        view.addSubview(hostingController.view)

        // Set constraints as needed
        hostingController.view.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            hostingController.view.topAnchor.constraint(equalTo: view.topAnchor),
            hostingController.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            hostingController.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),             hostingController.view.trailingAnchor.constraint(equalTo: view.trailingAnchor)
        ])

        // Notify the hostingController that it has moved to a parent view controller
        hostingController.didMove(toParent: self)
    }
}

func createCameraPreview() -> UIViewController {
    let swiftUIView = CameraPreview().edgesIgnoringSafeArea(.all)
    let hostingController = UIHostingController(rootView: swiftUIView)
    return hostingController
}
